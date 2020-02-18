package com.apps.mysweet.veiw;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.apps.mysweet.R;
import com.apps.mysweet.adapter.AdapterPhoto;
import com.apps.mysweet.adapter.CategoriesAdapter;
import com.apps.mysweet.adapter.ProductsHomeAdapter;
import com.apps.mysweet.adapter.VideoAdapter;
import com.apps.mysweet.model.AppVideo;
import com.apps.mysweet.model.Category;
import com.apps.mysweet.model.Product;
import com.apps.mysweet.viewmodel.HomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public interface onFragmentListener {
        void categorySelected(Category category);
        void imageProfileClicked();
    }


    private ViewPager viewPagerAdsFree;
    private int[] imagesIds = new int[]{
            R.drawable.img_free_1,
            R.drawable.img_free_2,
            R.drawable.img_free_3
    };
    private int[] imagesPhotosSales = new int[]{
            R.drawable.photo1,
            R.drawable.photo2,
            R.drawable.photo3
    };
    private EditText filterEditeText;
    private ImagesAdapter imagesAdapter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView bestProductRecyclerView;
    private RecyclerView photoSalesRecycler;
private Button btnMap;
    private CategoriesAdapter categoriesAdapter;
    private ProductsHomeAdapter productsHomeAdapter;
    ProductsHomeAdapter productsHomeAdapterRecommnded;
    private RecyclerView recommendedProductRecyclerView;
    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    private  ImageView profileImageView;
    HomeViewModel homeViewModel;
    private onFragmentListener onFragmentListener;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentListener) {
            onFragmentListener = (HomeFragment.onFragmentListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMap=view.findViewById(R.id.btn_map_view);
        viewPagerAdsFree = view.findViewById(R.id.view_pager_sell);
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categeory);
        filterEditeText = view.findViewById(R.id.edit_text_filter);
        bestProductRecyclerView = view.findViewById(R.id.best_item_list);
        photoSalesRecycler = view.findViewById(R.id.photos_sales_list);
        recommendedProductRecyclerView = view.findViewById(R.id.recommended_list);
        videoRecyclerView = view.findViewById(R.id.video_list);
       profileImageView=view.findViewById(R.id.image_view_profile);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        imagesAdapter = new ImagesAdapter(imagesIds);
        viewPagerAdsFree.setAdapter(imagesAdapter);
        //  createFilterEditTExt();
        createRecyclerViewCategories();
        createRecyclerViewBestProducts();
        createPhotsSalesList();
        createRecyclerViewRecommendedProducts();
        createRecyclerViewVideo();
        createProfileImageView();
        onClickBtnMap();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (categoriesAdapter != null)
            categoriesAdapter.startListening();
        if (productsHomeAdapter != null)
            productsHomeAdapter.startListening();
        if (productsHomeAdapterRecommnded != null)
            productsHomeAdapterRecommnded.startListening();
      if (videoAdapter!=null)
        videoAdapter.startListening();


    }

    private void createProfileImageView() {
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        onFragmentListener.imageProfileClicked();
            }
        });
    }
    private void createFilterEditTExt() {

    }

    private void createRecyclerViewCategories() {

        recyclerViewCategories.setLayoutManager(getLinearManger(LinearLayoutManager.HORIZONTAL));

        homeViewModel.getCategoriesData().observe(this, new Observer<FirestoreRecyclerOptions<Category>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Category> categoryFirestoreRecyclerOptions) {
                categoriesAdapter = new CategoriesAdapter(categoryFirestoreRecyclerOptions);
                recyclerViewCategories.setAdapter(categoriesAdapter);
                categoriesAdapter.startListening();
                categoriesAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(Category category) {
                        if (onFragmentListener != null)
                            onFragmentListener.categorySelected(category);
                    }
                });

            }
        });
        Log.d("ttt", "in out ");


    }

    private void createRecyclerViewBestProducts() {
        bestProductRecyclerView.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
        homeViewModel.getDataBestProducts().observe(this, new Observer<FirestoreRecyclerOptions<Product>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Product> productFirestoreRecyclerOptions) {
                productsHomeAdapter = new ProductsHomeAdapter(productFirestoreRecyclerOptions);
                bestProductRecyclerView.setAdapter(productsHomeAdapter);
                productsHomeAdapter.startListening();
            }
        });
    }

    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(orientation);
        return llm;
    }


    private void createPhotsSalesList() {
        photoSalesRecycler.setLayoutManager(getLinearManger(LinearLayoutManager.HORIZONTAL));
        AdapterPhoto adapterPhoto = new AdapterPhoto(imagesPhotosSales);
        photoSalesRecycler.setAdapter(adapterPhoto);

    }

    private void createRecyclerViewRecommendedProducts() {

        recommendedProductRecyclerView.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
        homeViewModel.getDataRecommendedProducts().observe(this, new Observer<FirestoreRecyclerOptions<Product>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Product> productFirestoreRecyclerOptions) {
                productsHomeAdapterRecommnded = new ProductsHomeAdapter(productFirestoreRecyclerOptions);
                productsHomeAdapterRecommnded.startListening();
                recommendedProductRecyclerView.setAdapter(productsHomeAdapterRecommnded);

            }
        });
    }


    private void createRecyclerViewVideo() {
        videoRecyclerView.setLayoutManager(getLinearManger(LinearLayoutManager.HORIZONTAL));
       homeViewModel.getDataVideo().observe(this, new Observer<FirestoreRecyclerOptions<AppVideo>>() {
           @Override
           public void onChanged(FirestoreRecyclerOptions<AppVideo> appVideoFirestoreRecyclerOptions) {
                videoAdapter = new VideoAdapter(appVideoFirestoreRecyclerOptions);
            videoAdapter.startListening();;
               videoRecyclerView.setAdapter(videoAdapter);

           }
       });

    }
    public void onClickBtnMap(){
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext().getApplicationContext(),MapsActivity.class));
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        categoriesAdapter.stopListening();
        productsHomeAdapter.stopListening();
        productsHomeAdapterRecommnded.stopListening();
        videoAdapter.stopListening();
    }

    class ImagesAdapter extends PagerAdapter {
        int[] imagesIds;

        public ImagesAdapter(int[] imagesIds) {
            this.imagesIds = imagesIds;
        }

        @Override
        public int getCount() {
            return imagesIds.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_ads, container, false);
            imageView.setImageResource(imagesIds[position]);
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

}
