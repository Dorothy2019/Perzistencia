package hu.bme.aut.android.todo.model

class Todo(
    val id: Long? = null,
    val title: String,
    val priority: Priority,
    val dueDate: String,
    val description: String
) {

    enum class Priority {
        LOW, MEDIUM, HIGH
    }

}