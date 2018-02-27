package br.com.allin.mobile.pushnotification.service.btg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.com.allin.mobile.pushnotification.entity.btg.AIWish;

/**
 * Created by lucasrodrigues on 07/02/18.
 */

public class WishListService extends TrackingBaseService<AIWish> {
    public WishListService(String account, List<AIWish> wishList) {
        super(account, "wishlist", wishList);
    }

    @Override
    JSONArray transform(List<AIWish> list) {
        try {
            JSONArray jsonArray = new JSONArray();

            for (AIWish search : list) {
                JSONObject productJSONObject = new JSONObject();
                productJSONObject.put("productId", search.getProductId());
                productJSONObject.put("active", search.isActive());

                jsonArray.put(productJSONObject);
            }

            return jsonArray;
        } catch (Exception e) {
            return null;
        }
    }
}
