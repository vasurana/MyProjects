package info.androidhive.palpalclient.helper;

import info.androidhive.palpalclient.R;
import info.androidhive.palpalclient.app.AppController;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class ProductListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Product> products;
	private ProductListAdapterListener listener;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public ProductListAdapter(Activity activity, List<Product> feedItems,
			ProductListAdapterListener listener) {
		this.activity = activity;
		this.products = feedItems;
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int location) {
		return products.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_item_product, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.productName);
		TextView description = (TextView) convertView
				.findViewById(R.id.productDescription);
		TextView price = (TextView) convertView.findViewById(R.id.productPrice);

		NetworkImageView image = (NetworkImageView) convertView
				.findViewById(R.id.productImage);

		Button btnAddToCart = (Button) convertView
				.findViewById(R.id.btnAddToCart);

		final Product product = products.get(position);

		name.setText(product.getName());

		description.setText(product.getDescription());

		price.setText("Price: $" + product.getPrice());

		// user profile pic
		image.setImageUrl(product.getImage(), imageLoader);

		btnAddToCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onAddToCartPressed(product);
				
			}
			
		});

		return convertView;
	}

	public interface ProductListAdapterListener {
		public void onAddToCartPressed(Product product);
		
	}

}