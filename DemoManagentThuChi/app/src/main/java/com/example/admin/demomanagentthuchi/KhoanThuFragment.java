package com.example.admin.demomanagentthuchi;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.demomanagentthuchi.Model.Management_khoanThu;
import com.example.admin.demomanagentthuchi.Model.Management_loaiThu;
import com.example.admin.demomanagentthuchi.MyAdapter.Adapter_KhoanThu;
import com.example.admin.demomanagentthuchi.MyAdapter.Adapter_SpinnerLoaiThu;
import com.example.admin.demomanagentthuchi.SQLite.khoanThuDAO;
import com.example.admin.demomanagentthuchi.SQLite.loaiThuDAO;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class KhoanThuFragment extends Fragment {
    public FloatingActionButton addKhoanThu;
    public EditText et_KhoanThu, et_NoidungKhoanThu;
    CalendarView calendarView;
    String date;

    SwipeRefreshLayout swipeRefreshLayout;


    //recycler
    RecyclerView recyclerViewKhoanThu;
    public ArrayList<Management_khoanThu> listKhoanThu = new ArrayList<Management_khoanThu>();
    //recycler

    //spinner
    public Spinner spLoaiThu;
    loaiThuDAO loaiThuDAO;
    ArrayList<Management_loaiThu> listLoaiThu = new ArrayList<Management_loaiThu>();
    //spinner

    //search
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    View view;
    Adapter_KhoanThu adapter_khoanThu;
    //search
    khoanThuDAO khoanThuDAO;
    Management_khoanThu khoanThu;

    public KhoanThuFragment() {
        // Required empty public constructor
    }

    //have to import this void new
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // have to import this void.
    // Create option menu for toolbar equal lookup, Just use for fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Have to add Clear with tabLayout
        menu.clear();
        //Have to add Clear with tablayou
        inflater.inflate(R.menu.menu_for_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    //important
                    adapter_khoanThu.getFilter().filter(newText);
                    //important

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
    //create option menu for toolbar equal lookup, Just use for fragment


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_khoan_thu, null);
        addKhoanThu = view.findViewById(R.id.addKhoanThu);


        //refreshLayout use support v4
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        CapNhatGiaoDienKhoanThu();
                        TastyToast.makeText(getContext(), "Hoàn tất !", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        //Toasty.success(getContext(), " Đã cập nhật! ", Toast.LENGTH_SHORT, true).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        //refreshLayout


        //RecyclerView
        recyclerViewKhoanThu = view.findViewById(R.id.recyclerViewKhoanThu);
        khoanThuDAO = new khoanThuDAO(getContext());
        CapNhatGiaoDienKhoanThu();
        //RecyclerView


        //even of dialog
        addKhoanThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                LayoutInflater inf = KhoanThuFragment.this.getLayoutInflater();
                View view1 = inf.inflate(R.layout.dialog_khoanthu, null);

                et_KhoanThu = view1.findViewById(R.id.et_KhoanThu);
                et_NoidungKhoanThu = view1.findViewById(R.id.et_NoidungKhoanThu);

                //Xu ly spinner
                spLoaiThu = view1.findViewById(R.id.sp_LoaiThu);
                loaiThuDAO = new loaiThuDAO(getContext());
                CapNhatGiaoDienSpinnerKhoanThu();
                //Xu ly spinner

                //date
                calendarView = view1.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    }
                });
                calendarView.setSelected(true);
                //date

                alertDialog.setView(view1);

                alertDialog.setNegativeButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tenKhoanThu = et_KhoanThu.getText().toString();
                        String noiDung = et_NoidungKhoanThu.getText().toString();
                        //date
                        String getDate = date;
                        //date


                        //spinner
                        int _idLoaiThu = 0;
                        try {
                            int index = spLoaiThu.getSelectedItemPosition();
                            Management_loaiThu loaiThu = listLoaiThu.get(index);
                            _idLoaiThu = loaiThu._id;

                        } catch (Exception e) {

                        }
                        //spinner


                        //SQlite

                        khoanThu = new Management_khoanThu(tenKhoanThu, noiDung, getDate, _idLoaiThu);
                        khoanThuDAO.addKhoanThu(khoanThu);
                        //SQlite


                        CapNhatGiaoDienKhoanThu();
                        Toasty.success(getContext(), "Đã thêm!", Toast.LENGTH_SHORT, true).show();

                    }
                });
                alertDialog.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.error(getContext(), "Đã hủy", Toast.LENGTH_SHORT, true).show();
                    }
                });

                alertDialog.show();
                //Dialog
            }
        });
        //even of dialog

        return view;
    }


    public void CapNhatGiaoDienKhoanThu() {

        listKhoanThu = khoanThuDAO.ViewKhoanThu();
        //
        adapter_khoanThu = new Adapter_KhoanThu(getContext(), listKhoanThu, listLoaiThu, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewKhoanThu.setLayoutManager(linearLayoutManager);
        recyclerViewKhoanThu.setAdapter(adapter_khoanThu);


    }

    public void CapNhatGiaoDienSpinnerKhoanThu() {
        listLoaiThu = loaiThuDAO.ViewLoaiThu();
        Adapter_SpinnerLoaiThu spinnerLoaiThu = new Adapter_SpinnerLoaiThu(getContext(), listLoaiThu);
        spLoaiThu.setAdapter(spinnerLoaiThu);
    }


    //CapNhatGiaoDienChoFragmentKhi Chuyen TabLayout
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    //CapNhatGiaoDienChoFragmentKhi chuyen TabLayou

}
