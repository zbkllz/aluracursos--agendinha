package laros.com.ui.activity;

import static laros.com.ui.activity.ConstantActivities.STUDENT_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import laros.com.R;
import laros.com.dao.StudentDAO;
import laros.com.model.Student;

public class StudentListActivity extends AppCompatActivity {

    private static final String TITLE_APPBAR = "Students List";
    private final StudentDAO dao = new StudentDAO();
    private ArrayAdapter<Student> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        setTitle(TITLE_APPBAR);
        fabConfigEnrollStudent();
        listConfig();

        for(int i = 0; i < 10; i++){
            dao.save(new Student("Fulano", "1122223333", "fuleco@alura.com.br"));
            dao.save(new Student("Cicrana", "1122223333", "cricri@gmail.com"));
            dao.save(new Student("Beltrano", "1122224444", "bebel@gmail.com"));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_students_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_students_list_menu_delete) {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Student selectedStudent = adapter.getItem(menuInfo.position);
            remove(selectedStudent);
        }
        return super.onContextItemSelected(item);
    }

    private void fabConfigEnrollStudent() {
        FloatingActionButton btnAddStudent = findViewById(R.id.fab_add_new_student);
        btnAddStudent.setOnClickListener(v -> openFormInsertMode());
    }

    private void openFormInsertMode() {
        startActivity(new Intent(this,
                StudentFormActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStudents();

    }

    private void refreshStudents() {
        adapter.clear();
        adapter.addAll(dao.all());
    }

    private void listConfig() {
        ListView studentsList = findViewById(R.id.lv_students_list);
        adapterConfig(studentsList);
        configClickListenerPerItem(studentsList);
        configLongClickItemListener(studentsList);
        registerForContextMenu(studentsList);
    }

    private void configLongClickItemListener(ListView studentsList) {
        studentsList.setOnItemLongClickListener((adapterView, view, position, id) -> {
            Student selectedStudent = (Student) adapterView.getItemAtPosition(position);
            remove(selectedStudent);
            return true;
        });
    }

    private void remove(Student student) {
//        removingAlert(student);
//        adapter.notifyDataSetChanged();
        adapter.remove(student);
        dao.remove(student);
    }

//    private void removingAlert(Student student) {
//        new AlertDialog.Builder(this)
//                .setTitle("Deleting")
//                .setMessage("Are you sure?")
//                .setPositiveButton("Aw Yeah!", (dialog, which) -> {
//                    adapter.notifyDataSetChanged();
//                    dao.remove(student);
//                })
//                .setNegativeButton("Nope!",null)
//                .show();
//    }

    private void configClickListenerPerItem(ListView studentsList) {
        studentsList.setOnItemClickListener((adapterView, view, position, id) -> {
            Student selectedStudent = (Student) adapterView.getItemAtPosition(position);
            openFormEditMode(selectedStudent);
        });
    }

    private void openFormEditMode(Student student) {
        Intent goToStudentFormActivity = new Intent(StudentListActivity.this,
                StudentFormActivity.class);
        goToStudentFormActivity.putExtra(STUDENT_KEY, student);
        startActivity(goToStudentFormActivity);
    }

    private void adapterConfig(ListView studentsList) {
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1);
        studentsList.setAdapter(adapter);
    }


}
