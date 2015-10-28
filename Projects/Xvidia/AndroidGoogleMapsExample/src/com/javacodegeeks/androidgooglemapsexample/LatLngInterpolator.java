package com.javacodegeeks.androidgooglemapsexample;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import com.google.android.gms.maps.model.LatLng;

public interface LatLngInterpolator {
	public LatLng interpolate(float fraction, LatLng a, LatLng b);

	public class Linear implements LatLngInterpolator {
		@Override
		public LatLng interpolate(float fraction, LatLng a, LatLng b) {
			double lat = (b.latitude - a.latitude) * fraction + a.latitude;
			double lng = (b.longitude - a.longitude) * fraction + a.longitude;
			return new LatLng(lat, lng);
		}
	}

	public class LinearFixed implements LatLngInterpolator {
		@Override
		public LatLng interpolate(float fraction, LatLng a, LatLng b) {
			double lat = (b.latitude - a.latitude) * fraction + a.latitude;
			double lngDelta = b.longitude - a.longitude;

			if (Math.abs(lngDelta) > 180) {
				lngDelta -= Math.signum(lngDelta) * 360;
			}
			double lng = lngDelta * fraction + a.longitude;
			return new LatLng(lat, lng);
		}
	}

	public class Spherical implements LatLngInterpolator {

		@Override
		public LatLng interpolate(float fraction, LatLng from, LatLng to) {

			double fromLat = toRadians(from.latitude);
			double fromLng = toRadians(from.longitude);
			double toLat = toRadians(to.latitude);
			double toLng = toRadians(to.longitude);
			double cosFromLat = cos(fromLat);
			double cosToLat = cos(toLat);

			double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
			double sinAngle = sin(angle);
			if (sinAngle < 1E-6) {
				return from;
			}
			double a = sin((1 - fraction) * angle) / sinAngle;
			double b = sin(fraction * angle) / sinAngle;

			double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
			double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
			double z = a * sin(fromLat) + b * sin(toLat);

			double lat = atan2(z, sqrt(x * x + y * y));
			double lng = atan2(y, x);
			return new LatLng(toDegrees(lat), toDegrees(lng));
		}

		private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {

			double dLat = fromLat - toLat;
			double dLng = fromLng - toLng;
			return 2 * asin(sqrt(pow(sin(dLat / 2), 2) + cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
		}
	}
}
