package onryde.startupapp;

import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;
import android.widget.TableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Writer;


public class MainActivity extends ActionBarActivity {

    public String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSubmit(View v) throws Exception {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

        TableRow tableRowName = (TableRow) tableLayout.getChildAt(0);
        EditText username = (EditText) tableRowName.getChildAt(1);
        String name = username.getText().toString();

        TableRow tableRowEmail = (TableRow) tableLayout.getChildAt(1);
        EditText userEmail = (EditText) tableRowEmail.getChildAt(1);
        String email = userEmail.getText().toString();

        TableRow tableRowPassword = (TableRow) tableLayout.getChildAt(2);
        EditText userPassword = (EditText) tableRowPassword.getChildAt(1);
        String password = userPassword.getText().toString();

        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.gender);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        RadioButton genderButton = (RadioButton) findViewById(selectedId);
        String gender = genderButton.getText().toString();

        Spinner mySpinner = (Spinner) findViewById(R.id.age_group);
        String age = mySpinner.getSelectedItem().toString();

        JSONObject userjson = new JSONObject();
        try {
            userjson.put("name", name);
            userjson.put("password", password);
            userjson.put("gender", gender);
            userjson.put("age", age);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String text = userjson.toString();
//        String dirPath = getApplicationInfo().dataDir;
        String dirPath = "/mnt/sdcard/test/";
        filePath = new File(dirPath, "users.json").toString();
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                writeStringToFile("{}", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            String jsonData = getStringFromFile(filePath);
            JSONObject jO = new JSONObject(jsonData);
            if (jO.has(email)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                TextView myMsg = new TextView(this);
                myMsg.setText("Authorize Alert!");
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextSize(20);
                myMsg.setTextColor(Color.WHITE);
                //set custom title
                builder.setCustomTitle(myMsg);
                builder.setMessage("Email:" + email + " already exists!!!");
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.show();
            } else {
                jO.put(email, userjson);
                writeStringToFile(jO.toString(), file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView myMsg = new TextView(this);
        myMsg.setText("Authorize Alert!");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        myMsg.setTextSize(20);
        myMsg.setTextColor(Color.WHITE);
        //set custom title
        builder.setCustomTitle(myMsg);
        builder.setMessage(userjson.toString());
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();

    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static void writeStringToFile(String inputString, File file) throws Exception {
        Writer output = null;
        output = new BufferedWriter(new FileWriter(file));
        output.write(inputString);
        output.close();
    }
}
