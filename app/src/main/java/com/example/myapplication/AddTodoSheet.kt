package com.example.myapplication


import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentAddTodoBinding
import com.example.myapplication.entities.TodoEntity
import com.example.myapplication.view.model.TodoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.sql.Time
import java.time.LocalTime

class AddTodoSheet(var todoItem: TodoEntity?) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentAddTodoBinding
    private lateinit var todoViewModel: TodoViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if (todoItem != null)
        {
            binding.taskTitle.text = "Edit Todo"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(todoItem!!.name)
            binding.desc.text = editable.newEditable(todoItem!!.desc)
            if(todoItem!!.dueTime != null){
                dueTime = todoItem!!.dueTime!!
                updateTimeButtonText()
            }
        }
        else
        {
            binding.taskTitle.text = "Add Todo"
        }

        todoViewModel = ViewModelProvider(activity)[TodoViewModel::class.java]
        binding.saveButton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()

    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddTodoBinding.inflate(inflater,container,false)
        return binding.root
    }


    private fun saveAction()
    {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        if(todoItem == null)
        {
            val newTask = TodoEntity(name,desc,dueTime,null)
            todoViewModel.addTodoItem(newTask)
        }
        else
        {
            todoViewModel.updateTodoItem(todoItem!!.id, name, desc, dueTime)
        }
        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
    }

}








