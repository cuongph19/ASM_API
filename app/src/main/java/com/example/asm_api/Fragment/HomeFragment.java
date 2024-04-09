package com.example.asm_api.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.asm_api.R;
import com.example.asm_api.adapter.ProductAdapter;
import com.example.asm_api.databinding.FragmentHomeBinding;
import com.example.asm_api.model.Fruit;
import com.example.asm_api.model.Page;
import com.example.asm_api.model.Response;
import com.example.asm_api.services.HttpRequest;
import com.example.asm_api.view.FruitDetailActivity;
import com.example.asm_api.view.UpdateFruitActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements ProductAdapter.FruitClick {
    FragmentHomeBinding binding;
    private HttpRequest httpRequest;
    private SharedPreferences sharedPreferences;
    private String token;
    private ProductAdapter adapter;
    private ArrayList<Fruit> ds = new ArrayList<>();
    private int page = 1;
    private int totalPage = 0;
    private String sort = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Khởi tạo RecyclerView và gán adapter
        adapter = new ProductAdapter(requireContext(), new ArrayList<>(), this); // Tạo adapter mới với danh sách rỗng
        binding.rcvFruit.setAdapter(adapter);
        binding.rcvFruit.setLayoutManager(new LinearLayoutManager(requireContext()));

        sharedPreferences = requireActivity().getSharedPreferences("INFO", requireContext().MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        httpRequest = new HttpRequest(token);

        Map<String, String> map = getMapFilter(page, "", "0", "-1");
        httpRequest.callAPI().getPageFruit(map)
                .enqueue(getListFruitResponse);

        config();

        userListener();

        return view;
    }

    private void config() {
        binding.nestScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.d("33333333333", "onScrollChange: 123" + totalPage + "  page" + page);
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (totalPage == page) return;
                    if (binding.loadmore.getVisibility() == View.GONE) {
                        binding.loadmore.setVisibility(View.VISIBLE);
                        page++;
                        FilterFruit();
                    }
                }
            }
        });

        // Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.spinner_price, android.R.layout.simple_spinner_item);
        binding.spinner.setAdapter(spinnerAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence value = (CharSequence) parent.getAdapter().getItem(position);
                Log.d("zzzzzz", "onItemSelected: " + value.toString());
                if (value.toString().equals("Ascending")) {
                    sort = "1";
                } else if (value.toString().equals("Decrease")) {
                    sort = "-1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinner.setSelection(1);
    }

    private void userListener() {

        binding.btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ds.clear();
                FilterFruit();
            }
        });
    }

    Callback<Response<Page<ArrayList<Fruit>>>> getListFruitResponse = new Callback<Response<Page<ArrayList<Fruit>>>>() {
        @Override
        public void onResponse(Call<Response<Page<ArrayList<Fruit>>>> call, retrofit2.Response<Response<Page<ArrayList<Fruit>>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    totalPage = response.body().getData().getTotalPage();
                    ArrayList<Fruit> _ds = response.body().getData().getData();
                    getData(_ds);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Page<ArrayList<Fruit>>>> call, Throwable t) {
            Log.e("getListFruitResponse", "onFailure: " + t.getMessage());
        }
    };

    private void getData(ArrayList<Fruit> _ds) {
        Log.d("zzzzzzzz", "getData: " + _ds.size());

        if (binding.loadmore.getVisibility() == View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyItemInserted(ds.size() - 1);
                    binding.loadmore.setVisibility(View.GONE);
                    ds.clear();
                    ds.addAll(_ds);
                    adapter.notifyDataSetChanged();

                }
            }, 1000);
            return;
        } else {
            ds.clear();
            ds.addAll(_ds);
            adapter = new ProductAdapter(requireContext(), ds, HomeFragment.this);
            binding.rcvFruit.setAdapter(adapter);
            binding.rcvFruit.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }

    private void FilterFruit() {
        String _name = binding.edSearchName.getText().toString().equals("") ? "" : binding.edSearchName.getText().toString();
        String _price = binding.edSearchMoney.getText().toString().equals("") ? "0" : binding.edSearchMoney.getText().toString();
        String _sort = sort.equals("") ? "-1" : sort;
        Map<String, String> map = getMapFilter(page, _name, _price, _sort);
        httpRequest.callAPI().getPageFruit(map).enqueue(getListFruitResponse);
    }

    private Map<String, String> getMapFilter(int _page, String _name, String _price, String _sort) {
        Map<String, String> map = new HashMap<>();

        map.put("page", String.valueOf(_page));
        map.put("name", String.valueOf(_name));
        map.put("price", String.valueOf(_price));
        map.put("sort", String.valueOf(_sort));

        return map;
    }

    Callback<Response<Fruit>> responseFruitAPI = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    page = 1;
                    ds.clear();
                    FilterFruit();

                    Toast.makeText(requireContext(), response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {
            Log.e("responseFruitAPI", "onFailure: " + t.getMessage());
        }
    };

    @Override
    public void delete(Fruit fruit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc mình muốn xóa");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callAPI()
                    .deleteFruits(fruit.get_id())
                    .enqueue(responseFruitAPI);
        });
        builder.setNegativeButton("no", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    @Override
    public void edit(Fruit fruit) {
        Intent intent = new Intent(requireActivity(), UpdateFruitActivity.class);
        intent.putExtra("fruit", fruit);
        startActivity(intent);
    }

    @Override
    public void showDetail(Fruit fruit) {
        Intent intent = new Intent(requireActivity(), FruitDetailActivity.class);
        intent.putExtra("fruit", fruit);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("loadddddd", "onResume: ");
        page = 1;
        ds.clear();
        FilterFruit();
    }
    @Override
    public void onFruitClick(Fruit fruit) {
        // Gọi phương thức showDetail khi click vào item
        showDetail(fruit);
    }
}