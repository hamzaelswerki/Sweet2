package com.apps.mysweet.veiw;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.MyProductsAdapter;
import com.apps.mysweet.model.Order;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.model.User;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.apps.mysweet.viewmodel.UserInfoViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment {
    private static RecyclerView listMyProduct;
    private static OrderViewModel orderViewModel;
    private MyProductsAdapter myProductAdapter;

    private TextView subTotalTv;
    private TextView totalTv;
    private TextView deliveryTv;
    private EditText dateTimeInputEditText;
    private TextView addressTv;
    private ModalSheetAdressList sheetAdressList;
    private Button btnConfirm;
    int total;
    UserInfoViewModel userInfoViewModel;
   Timestamp timestamp4;
    Calendar localCalender;
    public interface FirestoreCallBack {
        void onCallBack(int subTotalT);

    }


    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listMyProduct = view.findViewById(R.id.recyclerView);
        subTotalTv = view.findViewById(R.id.textView34);
        totalTv = view.findViewById(R.id.textView37);
        deliveryTv = view.findViewById(R.id.textView35);
        dateTimeInputEditText = view.findViewById(R.id.date_edite_text);
        addressTv = view.findViewById(R.id.adress_textView);
        btnConfirm = view.findViewById(R.id.button3_confirm);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userInfoViewModel = ViewModelProviders.of(this).get(UserInfoViewModel.class);

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        createRecyclerViewMyProducts();
        createAdreessTextView();
        getSubTotaData();
        createDateEditeText();


        createConfirmButton();
        getOrderAddess();

    }

    void getOrderAddess() {
        orderViewModel.getOrder().observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order.getAddress() != null) {
                    addressTv.setText(order.getAddress());
                }
            }
        });
        getAddressFromBundleWhenSelectFormAdapter();
    }

    private void getAddressFromBundleWhenSelectFormAdapter() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String a = bundle.getString("Address", null);
            if (a != null) {
                addressTv.setText(getArguments().getString("Address"));

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (myProductAdapter != null)
            myProductAdapter.startListening();
    }


    private void createAdreessTextView() {
        sheetAdressList = new ModalSheetAdressList(this);

        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderViewModel.getListTitles().observe(getActivity(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        if (strings.size() > 0) {
                            if (!sheetAdressList.isVisible())
                                sheetAdressList.show(getFragmentManager(), sheetAdressList.getTag());
                        } else {
                            Intent intent = new Intent(getContext(), MapsSelectPlaceActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }

        });
    }

    private void getSubTotaData() {

        orderViewModel.getSubTotalData(new FirestoreCallBack() {
            @Override
            public void onCallBack(int subTotalT) {
                subTotalTv.setText("₪ " + subTotalT + ".00");
                deliveryTv.setText("₪ " + 6 + ".00");
                subTotalT = (int) (Double.parseDouble(deliveryTv.getText().toString().substring(1)) + subTotalT);
                total = subTotalT;
                totalTv.setText("₪ " + subTotalT + ".00");

            }
        });
    }


    private void createRecyclerViewMyProducts() {

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        listMyProduct.setLayoutManager(llm);

        orderViewModel.getListMyProducts().observe(this, new Observer<FirestoreRecyclerOptions<Product>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Product> productFirestoreRecyclerOptions) {
                myProductAdapter = new MyProductsAdapter(productFirestoreRecyclerOptions);
                myProductAdapter.startListening();
                myProductAdapter.setOnbuttonUpdatesLustener(new MyProductsAdapter.OnUpdateQuantity() {
                    @Override
                    public void onUpdated(final Product product) {
                        orderViewModel.updateProduct(product);
                        listMyProduct.setAdapter(myProductAdapter);

                        getSubTotaData();
                    }
                });

                listMyProduct.setAdapter(myProductAdapter);
            }
        });
    }


    private void createConfirmButton() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertDateToTimeStamp("");

                /*     orderViewModel.getTaskMyProducts().observe(getActivity(), new Observer<Task<QuerySnapshot>>() {
                    @Override
                    public void onChanged(Task<QuerySnapshot> task) {
                        if (!task.getResult().getDocuments().isEmpty()
                                //   && !TextUtils.isEmpty(dateTimeInputEditText.getText().toString())
                                && !TextUtils.isEmpty(addressTv.getText().toString())) {

                            Toast.makeText(getContext(), "Not empty", Toast.LENGTH_SHORT).show();
                            //get User name
                            userInfoViewModel.getUserInfo().observe(getActivity(), new Observer<User>() {
                                @Override
                                public void onChanged(User user) {
                                    final Order order = new Order(addressTv.getText().toString(),

                                            //   convertDateToTimeStamp(dateTimeInputEditText.getText().toString()),
                                            new Timestamp(1999,0),
                                            user.getUserName());
                                    Toast.makeText(getContext(), user.getUserName(), Toast.LENGTH_SHORT).show();

                                    //get listTitles for user
                                    orderViewModel.getListTitles().observe(getActivity(), new Observer<ArrayList<String>>() {
                                        @Override
                                        public void onChanged(ArrayList<String> strings) {
                                            //order.setListTitles(strings);
                                            order.setTotalAmount(total);
                                            Toast.makeText(getContext(), "sucess get list", Toast.LENGTH_SHORT).show();
                                            //add order
                                            FirebaseFirestore.getInstance().collection("orders")
                                                    .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                                                    .set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(), "added order successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Fauiler", Toast.LENGTH_SHORT).show();
                                                    Log.d("ttt", "error" + e.getMessage());
                                                }
                                            });
                                            Log.d("ttt", "eto");
                                        }
                                    });
                                }
                            });
                        } else {

                            Toast.makeText(getContext(), " please Enter All Field ", Toast.LENGTH_SHORT).show();

                        }
                    }

                });
           */
            }


        });
    }
    private void getCurrentDate(long time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date currenTimeZone=new java.util.Date((long)time);
        Toast.makeText(getContext(), sdf.format(currenTimeZone), Toast.LENGTH_SHORT).show();
        Log.d("ttt", "  is => " +  sdf.format(currenTimeZone));

    }
    private Timestamp convertDateToTimeStamp(String currentDate) {
        String str_date = "6.3.2020 9:09";
        SimpleDateFormat formatter = new SimpleDateFormat("d.M.yyyy H:mm", Locale.GERMANY);
         Date date = null;
        try {

            date = formatter.parse(str_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        getCurrentDate(Calendar.getInstance().getTime().getTime());


        java.sql.Timestamp timeStampDate = new java.sql.Timestamp(date.getTime());
        Log.d("ttt", "Date  is " + date.getTime());
        Log.d("ttt", "Time is " + timeStampDate.toString());
       int x= (int) date.getTime();

        final Timestamp timestamp = new Timestamp(x, 0);
        Log.d("ttt", "Time2 is " + timestamp.toString());
        Log.d("ttt", "Time2 is " + timestamp.toDate().getTime());

        return timestamp;
        /*
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.US);
        Date date = null;
        try {
            date = simpleDateFormat.parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Timestamp timestamp = new Timestamp(date.getSeconds(), 0);
        return timestamp;
  */
    }

    private void createDateEditeText() {

        dateTimeInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(dateTimeInputEditText.getText().toString())) {

                } else {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),

                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    Calendar localCalender = Calendar.getInstance();
                                    localCalender.set(Calendar.YEAR, year);
                                    localCalender.set(Calendar.MONTH, month);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy",
                                            Locale.US);
                                    String dateString = simpleDateFormat.format(new Date(localCalender.getTimeInMillis()));
                                    dateTimeInputEditText.setText(dateString);
                                }
                            }, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                    datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            Calendar localCalender = Calendar.getInstance();
                                            localCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                            localCalender.set(Calendar.MINUTE, minute);
                                            SimpleDateFormat simpleDateFormat =
                                                    new SimpleDateFormat("hh:mm aaa", Locale.US);
                                            String timeString = simpleDateFormat.
                                                    format(new Date(localCalender.getTimeInMillis()));


                                            /*if (TextUtils.isEmpty(dateTimeInputEditText.getText().toString())) {
                                                SimpleDateFormat ss = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                                String dateString = ss.format(new Date(calendar.getTimeInMillis()));
                                                dateTimeInputEditText.setText(dateString);
                                            }*/
                                            dateTimeInputEditText.append(" " + timeString);
                                        }
                                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                            timePickerDialog.show();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        myProductAdapter.stopListening();
    }
}
