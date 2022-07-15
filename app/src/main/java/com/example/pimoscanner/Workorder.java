package com.example.pimoscanner;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Workorder extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    private ConSQL conSQL;
    private Connection connection;
    private ArrayList<RowItemList> arrayList;
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private boolean check = false;
    private ProgressBar progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_workorder );

        //------------------------------------------Menu Item

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId ( R.id.work);

        String email = getIntent ().getStringExtra ( "emaillogin" );
        String password = getIntent ().getStringExtra ( "passwordlogin" );
        String name = getIntent ().getStringExtra ( "name" );
        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );

//        Toast.makeText ( this, "Hi Work+ '"+email+"'", Toast.LENGTH_SHORT ).show ();



        navigation.setOnNavigationItemSelectedListener ( new BottomNavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId ()){
                    case R.id.home:
                        String name = getIntent ().getStringExtra ( "name" );
                        String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String password = getIntent ().getStringExtra ( "passwordlogin" );

                        Intent intent = new Intent(Workorder.this, splash.class);
                        overridePendingTransition ( 0,0 );

                        intent.putExtra ( "emaillogin",email);
                        intent.putExtra ( "passwordlogin", password);
                        intent.putExtra ( "name", name );
                        intent.putExtra ( "jobTitle", jobTitle );

                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.work:

                        return true;

                    case R.id.camera:
                        String name1 = getIntent ().getStringExtra ( "name" );
                        String jobTitle1 = getIntent ().getStringExtra ( "jobTitle" );
                        String email1 = getIntent ().getStringExtra ( "emaillogin" );
                        String password1 = getIntent ().getStringExtra ( "passwordlogin" );

                        Intent intent1 = new Intent(Workorder.this, Camera.class);
                        overridePendingTransition ( 0,0 );

                        intent1.putExtra ( "emaillogin",email1);
                        intent1.putExtra ( "passwordlogin", password1);
                        intent1.putExtra ( "name", name1 );
                        intent1.putExtra ( "jobTitle", jobTitle1 );

                        startActivity(intent1);
                        finish();
                        return true;

                    case R.id.scan:
                        String name2 = getIntent ().getStringExtra ( "name" );
                        String jobTitle2 = getIntent ().getStringExtra ( "jobTitle" );
                        String email2 = getIntent ().getStringExtra ( "emaillogin" );
                        String password2 = getIntent ().getStringExtra ( "passwordlogin" );

                        Intent intent2 = new Intent(Workorder.this, MainActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent2.putExtra ( "emaillogin",email2);
                        intent2.putExtra ( "passwordlogin", password2);
                        intent2.putExtra ( "name", name2 );
                        intent2.putExtra ( "jobTitle", jobTitle2 );

                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.projects:
                        String name3 = getIntent ().getStringExtra ( "name" );
                        String jobTitle3 = getIntent ().getStringExtra ( "jobTitle" );
                        String email3 = getIntent ().getStringExtra ( "emaillogin" );
                        String password3 = getIntent ().getStringExtra ( "passwordlogin" );

                        Intent intent3 = new Intent(Workorder.this, MapsAppActivity.class);
                        overridePendingTransition ( 0,0 );

                        intent3.putExtra ( "emaillogin",email3);
                        intent3.putExtra ( "passwordlogin", password3);
                        intent3.putExtra ( "name", name3 );
                        intent3.putExtra ( "jobTitle", jobTitle3 );

                        startActivity(intent3);
                        finish();
                        return true;

                }
                return true;
            }
        } );
//
//        //-------------------------------------------------------------
//

        conSQL = new ConSQL ();
        connection = conSQL.connection ();

        recyclerView = findViewById ( R.id.recyclerView );
        recyclerView.setHasFixedSize ( true );
        layoutManager = new LinearLayoutManager ( this );
        recyclerView.setLayoutManager ( layoutManager );

        arrayList = new ArrayList<>();

        //For the Bottom border line in the workorder List
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager (Workorder.this,1));

        myTask();


    }

    public void myTask(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(connection == null){
                        check = false;
                    }else{
                        String email = getIntent ().getStringExtra ( "emaillogin" );
                        String name = getIntent ().getStringExtra ( "name" );
                        String query = "select * from workOrders3 where  email = '" + email+ "'";
                        //   String sql = "SELECT * FROM registration WHERE email = '" + emaillogin.getText() + "' AND password = '" + passwordlogin.getText() + "' ";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);

                        if(resultSet != null){
                            while (resultSet.next()) {
                                arrayList.add ( new RowItemList ( resultSet.getString ( "status" ),
                                                                  resultSet.getString ( "responsibleperson" ),
                                                                  resultSet.getString ( "type" ),
                                                                  resultSet.getString ( "workCenter" ) ,
                                                                  resultSet.getString ( "priority" ) ,
                                                                  resultSet.getString ( "description" ),
                                                                  resultSet.getDate ( "finishDate" )
                                        )
                                );
                            }
                            check = true;

                        }else{
                            check = false;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    check = false;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialog.dismiss();
                        if(check == false){
                            System.out.println("An error occurred while retrieving data");
                        }
                        else {
                            try {
                                adapter = new CustomAdapter(arrayList);
                                recyclerView.setAdapter(adapter);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        }).start();
    }



//Adepter
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
        private List<RowItemList>rowItemLists;


        public class ViewHolder extends RecyclerView.ViewHolder{
            private final TextView status;
            private final TextView type;
            private final TextView responsibleperson;
            private final TextView description;
            private final TextView priority;
            private final TextView dueDate;
            private final TextView workCenter;
//            private final TextView textView3;
            public ViewHolder(View view){
                super(view);
                responsibleperson = view.findViewById(R.id.person );
                type = view.findViewById(R.id.type1);
                status = view.findViewById(R.id.status );
                description = view.findViewById(R.id.description );
                priority = view.findViewById(R.id.priority );
                workCenter = view.findViewById(R.id.workCenter );
                dueDate = view.findViewById(R.id.dueDate );



//                textView3 = view.findViewById(R.id.dueDate);


            }
        }

    public CustomAdapter (List<RowItemList>itemLists){
        rowItemLists = itemLists;
    }

        @NonNull
        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            final RowItemList rowItemList = rowItemLists.get(position);
            holder.responsibleperson.setText(rowItemList.getResponsiblePerson ());
            holder.type.setText ( rowItemList.getType ());
            holder.status.setText ( rowItemList.getStatus ());
            holder.dueDate.setText ( rowItemList.getDueDate ().toString () );
//            holder.description.setText ( rowItemList.getDescription ());
//            holder.priority.setText ( rowItemList.getPriority ());
//            holder.workCenter.setText ( rowItemList.getWorkCenter ());

//            holder.textView3.setText(rowItemList.getDueDate());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(Workorder.this,"status = "+rowItemList.getStatus (),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Workorder.this, ExtendWorkOrder.class);
                    intent.putExtra ( "status", (rowItemList.getStatus()));
                    intent.putExtra ( "type", (rowItemList.getType ()));
                    intent.putExtra ( "responsibleperson", (rowItemList.getResponsiblePerson ()));
                    intent.putExtra ( "workCenter", (rowItemList.getWorkCenter ()));
                    intent.putExtra ( "priority", (rowItemList.getPriority ()));
                    intent.putExtra ( "description", (rowItemList.getDescription ()));
                    intent.putExtra ( "dueDate", (rowItemList.getDueDate ().toString ()));

                    String name = getIntent ().getStringExtra ( "name" );
                    String jobTitle = getIntent ().getStringExtra ( "jobTitle" );
                    String email = getIntent ().getStringExtra ( "emaillogin" );
                    String password = getIntent ().getStringExtra ( "passwordlogin" );


                    intent.putExtra ( "emaillogin",email);
                    intent.putExtra ( "passwordlogin", password);
                    intent.putExtra ( "name", name );
                    intent.putExtra ( "jobTitle", jobTitle );

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return rowItemLists.size();
        }
    }
}






