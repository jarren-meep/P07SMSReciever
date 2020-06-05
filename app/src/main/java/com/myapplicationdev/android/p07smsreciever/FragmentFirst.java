package com.myapplicationdev.android.p07smsreciever;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.ContentResolver;

import androidx.fragment.app.Fragment;

public class FragmentFirst extends Fragment {

    Button btnAddText, emailBtn;
    TextView tvFrag1,result1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        tvFrag1 = view.findViewById(R.id.tvFrag1);
        btnAddText = view.findViewById(R.id.btnAddTextFrag1);
        result1 = view.findViewById(R.id.result1);
        emailBtn = view.findViewById(R.id.emailBtn);

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, "jarrentjr@gmail.com");
                email.putExtra(Intent.EXTRA_SUBJECT, "hey");
                email.putExtra(Intent.EXTRA_TEXT, tvFrag1.getText().toString());
                startActivity(Intent.createChooser(email, "Send Email"));
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
                String filter="body LIKE ? AND body LIKE ?";
                // The matches for the ?
                String[] filterArgs = {"%late%", "%min%"};
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
                        if (!type.equalsIgnoreCase("1")) {
                            type = "Sent:";
                        }
                        smsBody += type + " " + address + "\n at " + date
                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                result1.setText(smsBody);
            }
        });

        return view;
    }
}
