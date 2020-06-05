package com.myapplicationdev.android.p07smsreciever;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FragmentSecond extends Fragment {

    Button btnAddText, emailBtn;
    TextView tvFrag2, result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container,false);
        tvFrag2 = view.findViewById(R.id.tvFrag2);
        btnAddText = view.findViewById(R.id.btnAddTextFrag2);
        result = view.findViewById(R.id.result2);
        emailBtn = view.findViewById(R.id.emailBtn);

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, "jarrentjr@gmail.com");
                email.putExtra(Intent.EXTRA_SUBJECT, "hey");
                email.putExtra(Intent.EXTRA_TEXT, tvFrag2.getText().toString());
                try {
                    startActivity(email);
                    Log.i("Finished sending email.", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity().getBaseContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create all messages URI
                Uri uri = Uri.parse("content://sms");
                // The columns we want
                //  date is when the message took place
                //  address is the number of the other party
                //  body is the message content
                //  type 1 is received, type 2 sent
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                // Get Content Resolver object from which to
                //  query the content provider
                ContentResolver cr = getActivity().getContentResolver();
                // Fetch SMS Message from Built-in Content Provider
                // The filter String
                String filter="address LIKE ? AND body LIKE ? OR ?";
                // The matches for the ?
                String[] filterArgs = {"5554", "", "%%"};
                // Fetch SMS Message from Built-in Content Provider

                Cursor cursor = cr.query(uri, reqCols, null, null, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                        } else {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                result.setText(smsBody);
            }
        });
        return view;
    }
}
