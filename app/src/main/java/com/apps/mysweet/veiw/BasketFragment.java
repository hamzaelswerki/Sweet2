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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.MyProductsAdapter;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.viewmodel.OrderViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class BasketFragment extends Fragment {
    public static RecyclerView listMyProduct;
    public static OrderViewModel orderViewModel;
    MyProductsAdapter myProductAdapter;

    TextView subTotalTv;
    TextView totalTv;
    TextView deliveryTv;
    EditText dateTimeInputEditText;
    TextView addressTv;


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
        dateTimeInputEditText=view.findViewById(R.id.date_edite_text);
        addressTv = view.findViewById(R.id.adress_textView);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        createRecyclerViewBestProducts();
        createAdreessTextView();
        getSubTotaData();
      createDateEditeText();

    }


    @Override
    public void onStart() {
        super.onStart();

        if (myProductAdapter != null)
            myProductAdapter.startListening();
    }


    private void createAdreessTextView() {
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent =new Intent(getContext(),MapsSelectPlaceActivity.class);
                   startActivity(intent);

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
                totalTv.setText("₪ " + subTotalT + ".00");
            }
        });
    }


    private void createRecyclerViewBestProducts() {

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
                        Log.d("ttt",product.getProductId()+"   =  "+ product.getQuantity()+"");

                                    orderViewModel.updateProduct(product);
                                   listMyProduct.setAdapter(myProductAdapter);

                                  getSubTotaData();
                    }
                });

                listMyProduct.setAdapter(myProductAdapter);


            }
        });

    }

    private void createDateEditeText() {

        dateTimeInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(dateTimeInputEditText.getText().toString())) {
                    }
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
                            localCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                            String dateString = simpleDateFormat.format(new Date(localCalender.getTimeInMillis()));
                            dateTimeInputEditText.setText(dateString);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aaa", Locale.US);
                                    String timeString = simpleDateFormat.format(new Date(localCalender.getTimeInMillis()));

                                    if (TextUtils.isEmpty(dateTimeInputEditText.getText().toString())) {
                                        SimpleDateFormat ss = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                        String dateString = ss.format(new Date(calendar.getTimeInMillis()));
                                        dateTimeInputEditText.setText(dateString);
                                        }
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
