package org.sssj.com.sseth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    RecyclerView NewsRecyclerView;
    private static final String TAG = "NewsFragment";
    List<News> newsList = new ArrayList<>();
    RequestQueue requestQueue;
    String News_Url = "http://sscoinmedia.tech/EthereumWebService/ethereumNews.php";
    private NativeAd nativeAd;

    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    View view;

    /**
     * StartAppAd object declaration
     */
    private StartAppAd startAppAd = new StartAppAd(getContext());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_fragments, container, false);

        StartAppSDK.init(getActivity(), "207706847", false);

        NewsRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        NewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //NewsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        requestQueue = MySingleton.getInstance(getActivity()).getRequestQueue();  //using singleton object
        Show_News();
        loadNativeAd();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void Show_News() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, News_Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // for 1st array url
                        Log.i("MyTest", "Response" + response + "");
                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            Log.i("MyTest", "Array--> " + jsonArray);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                Log.i("MyTest1", "--> " + o + "");
                                News Object_news = new News(o.getString("subject"), o.getString("date"), o.getString("details"));
                                newsList.add(Object_news);
                            }
                            NewsRecyclerView.setAdapter(new NewsAdapter(newsList, getContext()));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        // for 2st employee object url

                        /*try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("employees");
                            Log.i("ZZZZZ", "" + jsonArray.length());


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject o = jsonArray.getJSONObject(i);
                                Employee e = new Employee(o.getString("Name"), o.getString("Mobile"), o.getString("Address"));
                                empList.add(e);
                            }
                            NewsRecyclerView.setAdapter(new MyAdapter(empList, getApplicationContext()));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(stringRequest);
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd(getActivity(), "2170634523153169_2170634676486487");

        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd);
            }


        });

        // Request an ad
        nativeAd.loadAd();
    }

    private void inflateAd(NativeAd nativeAd) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdContainer = view.findViewById(R.id.native_ad_container);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
        nativeAdContainer.addView(adView);

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.

        ImageView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);

        // MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        /*nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());*/
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getAdSubtitle());


        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd);
/*
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
*/

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        //  nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
        nativeAd.registerViewForInteraction(adView, clickableViews);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
