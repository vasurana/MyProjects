package in.com.app.wsdl;

//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version  2.5
//
// Date Of Creation: 11/7/2013 11:45:15 AM
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code  Version
//
import in.com.app.wsdl.WS_Enums.SoapProtocolVersion;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;
import android.util.Log;


public class XMDS {

//	public String NAMESPACE = "urn:xmds";
	public String url = "";
	//	public String url = "http://192.168.1.10/xibo/xmds.php";
	public int timeOut = 60000;
	public IWsdl2CodeEvents eventHandler;
//	public SoapProtocolVersion soapVersion;

	private static final String TAG = XMDS.class.getName();

	private XMDS() {
	}

	public XMDS(String serverUrl) {
		url = serverUrl;
	}

	public XMDS(IWsdl2CodeEvents eventHandler) {
		this.eventHandler = eventHandler;
	}

	public XMDS(IWsdl2CodeEvents eventHandler, String url) {
		this.eventHandler = eventHandler;
		this.url = url;
	}

	public XMDS(IWsdl2CodeEvents eventHandler, String url, int timeOutInSeconds) {
		this.eventHandler = eventHandler;
		this.url = url;
		this.setTimeOut(timeOutInSeconds);
	}

	public void setTimeOut(int seconds) {
		this.timeOut = seconds * 1000;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void RegisterDisplayAsync(String serverKey, String hardwareKey,
									 String displayName, String version) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		RegisterDisplayAsync(serverKey, hardwareKey, displayName, version, null);
	}

	public void RegisterDisplayAsync(final String serverKey,
									 final String hardwareKey, final String displayName,
									 final String version, final List<HeaderProperty> headers)
			throws Exception {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected String doInBackground(Void... params) {
				return RegisterDisplay(serverKey, hardwareKey, displayName,
						version, headers);
			}

			@Override
			protected void onPostExecute(String result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("RegisterDisplay", result);
				}
			}
		}.execute();
	}

	public String RegisterDisplay(String serverKey, String hardwareKey,
								  String displayName, String version) {
		Log.d(TAG, "registering display serverKey:: " + serverKey
				+ " hardwareKey :: "+hardwareKey + " display Name:: " + displayName
				+ " version :: " + version);
		return RegisterDisplay(serverKey, hardwareKey, displayName, version,
				null);
	}

	public String RegisterDisplay(String serverKey, String hardwareKey,
								  String displayName, String version, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = false;
		SoapObject soapReq = new SoapObject("urn:xmds", "RegisterDisplay");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("displayName", displayName);
		soapReq.addProperty("version", version);
//		soapReq.addProperty("clientType", "android");
//		soapReq.addProperty("clientVersion", version);
//		soapReq.addProperty("clientCode", version);
//		soapReq.addProperty("operatingSystem", "android");
//		soapReq.addProperty("macAddress", "3c:91:57:14:14:71");
		String resultVariable = null;
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		httpTransport.debug = true;
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {

				httpTransport.call("urn:xmds/RegisterDisplay", soapEnvelope,
						headers);
			} else {

				httpTransport.call("urn:xmds/RegisterDisplay", soapEnvelope,headerPropertyArrayList);

			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						resultVariable = j.toString();
//						return resultVariable;
					} else if (obj != null && obj instanceof String) {
						resultVariable = (String) obj;
//						return resultVariable;
					}
				}
			}
		} catch (ConnectException e) {
			resultVariable= null;
		} catch (ConnectTimeoutException e) {

			resultVariable= null;
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
			resultVariable= null;
		}
		return resultVariable;
	}

	public void RequiredFilesAsync(String serverKey, String hardwareKey,
								   String version) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		RequiredFilesAsync(serverKey, hardwareKey, version, null);
	}

	public void RequiredFilesAsync(final String serverKey,
								   final String hardwareKey, final String version,
								   final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected String doInBackground(Void... params) {
				return RequiredFiles(serverKey, hardwareKey, version, headers);
			}

			@Override
			protected void onPostExecute(String result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("RequiredFiles", result);
				}
			}
		}.execute();
	}

	public String RequiredFiles(String serverKey, String hardwareKey,
								String version) {
		return RequiredFiles(serverKey, hardwareKey, version, null);
	}

	public String RequiredFiles(String serverKey, String hardwareKey,
								String version, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "RequiredFiles");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		String resultVariable = null;
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/RequiredFiles", soapEnvelope,
						headers);
			} else {
				httpTransport.call("urn:xmds/RequiredFiles", soapEnvelope, headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						resultVariable = j.toString();
//						return resultVariable;
					} else if (obj != null && obj instanceof String) {
						resultVariable = (String) obj;
//						return resultVariable;
					}
				}
			}
		} catch (ConnectException e) {
			resultVariable = null;
		} catch (ConnectTimeoutException e) {
			resultVariable = null;
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
			resultVariable = null;

		}
		return resultVariable;
	}

	public void GetFileAsync(String serverKey, String hardwareKey,
							 String filePath, String fileType, int chunkOffset, int chuckSize,
							 String version) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		GetFileAsync(serverKey, hardwareKey, filePath, fileType, chunkOffset,
				chuckSize, version, null);
	}

	public void GetFileAsync(final String serverKey, final String hardwareKey,
							 final String filePath, final String fileType,
							 final int chunkOffset, final int chuckSize, final String version,
							 final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, VectorByte>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected VectorByte doInBackground(Void... params) {
				return GetFile(serverKey, hardwareKey, filePath, fileType,
						chunkOffset, chuckSize, version, headers);
			}

			@Override
			protected void onPostExecute(VectorByte result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("GetFile", result);
				}
			}
		}.execute();
	}

	public VectorByte GetFile(String serverKey, String hardwareKey,
							  String fileId, String fileType, int chunkOffset, int chuckSize,
							  String version) {
		return GetFile(serverKey, hardwareKey, fileId, fileType, chunkOffset,
				chuckSize, version, null);
	}

	public VectorByte GetFile(String serverKey, String hardwareKey,
							  String fileId, String fileType, int chunkOffset, int chuckSize,
							  String version, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "GetFile");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
//		soapReq.addProperty("filePath", filePath);
		soapReq.addProperty("fileId", fileId);

		soapReq.addProperty("fileType", fileType);
		soapReq.addProperty("chunkOffset", chunkOffset);
		soapReq.addProperty("chuckSize", chuckSize);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		VectorByte resultVariable = null;
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/GetFile", soapEnvelope, headers);
			} else {
				httpTransport.call("urn:xmds/GetFile", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					SoapPrimitive j = (SoapPrimitive) obj;
					 resultVariable = new VectorByte(j);
					return resultVariable;
				}
			}
		} catch (ConnectException e) {
			resultVariable = null;
		} catch (ConnectTimeoutException e) {
			resultVariable = null;
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
			resultVariable = null;
		}
		return resultVariable;
	}

	public void ScheduleAsync(String serverKey, String hardwareKey,
							  String version) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		ScheduleAsync(serverKey, hardwareKey, version, null);
	}

	public void ScheduleAsync(final String serverKey, final String hardwareKey,
							  final String version, final List<HeaderProperty> headers)
			throws Exception {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected String doInBackground(Void... params) {
				return Schedule(serverKey, hardwareKey, version, headers);
			}

			@Override
			protected void onPostExecute(String result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("Schedule", result);
				}
			}
		}.execute();
	}

	public String Schedule(String serverKey, String hardwareKey, String version) {
		return Schedule(serverKey, hardwareKey, version, null);
	}

	public String Schedule(String serverKey, String hardwareKey,
						   String version, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "Schedule");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		String resultVariable = null;
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/Schedule", soapEnvelope, headers);
			} else {
				httpTransport.call("urn:xmds/Schedule", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						 resultVariable = j.toString();
//						return resultVariable;
					} else if (obj != null && obj instanceof String) {
						resultVariable = (String) obj;
//						return resultVariable;
					}
				}
			}
		} catch (ConnectException e) {
			resultVariable = null;
		} catch (ConnectTimeoutException e) {
			resultVariable = null;
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
			resultVariable = null;
		}
		return resultVariable;
	}

	public void RecieveXmlLogAsync(String serverKey, String hardwareKey,
								   String xml, String version) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		RecieveXmlLogAsync(serverKey, hardwareKey, xml, version, null);
	}

	public void RecieveXmlLogAsync(final String serverKey,
								   final String hardwareKey, final String xml, final String version,
								   final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return RecieveXmlLog(serverKey, hardwareKey, xml, version,
						headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("RecieveXmlLog", result);
				}
			}
		}.execute();
	}

	public boolean RecieveXmlLog(String serverKey, String hardwareKey,
								 String xml, String version) {
		return RecieveXmlLog(serverKey, hardwareKey, xml, version, null);
	}

	public boolean RecieveXmlLog(String serverKey, String hardwareKey,
								 String xml, String version, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "RecieveXmlLog");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("xml", xml);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/RecieveXmlLog", soapEnvelope,
						headers);
			} else {
				httpTransport.call("urn:xmds/RecieveXmlLog", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j
								.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return false;
	}

	public void BlackListAsync(String serverKey, String hardwareKey,
							   int mediaId, String type, String reason, String version)
			throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		BlackListAsync(serverKey, hardwareKey, mediaId, type, reason, version,
				null);
	}

	public void BlackListAsync(final String serverKey,
							   final String hardwareKey, final int mediaId, final String type,
							   final String reason, final String version,
							   final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return BlackList(serverKey, hardwareKey, mediaId, type, reason,
						version, headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("BlackList", result);
				}
			}
		}.execute();
	}

	public boolean BlackList(String serverKey, String hardwareKey, int mediaId,
							 String type, String reason, String version) {
		return BlackList(serverKey, hardwareKey, mediaId, type, reason,
				version, null);
	}

	public boolean BlackList(String serverKey, String hardwareKey, int mediaId,
							 String type, String reason, String version,
							 List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "BlackList");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("mediaId", mediaId);
		soapReq.addProperty("type", type);
		soapReq.addProperty("reason", reason);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/BlackList", soapEnvelope, headers);
			} else {
				httpTransport.call("urn:xmds/BlackList", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j
								.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return false;
	}

	public void SubmitLogAsync(String version, String serverKey,
							   String hardwareKey, String logXml) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		SubmitLogAsync(version, serverKey, hardwareKey, logXml, null);
	}

	public void SubmitLogAsync(final String version, final String serverKey,
							   final String hardwareKey, final String logXml,
							   final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return SubmitLog(version, serverKey, hardwareKey, logXml,
						headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("SubmitLog", result);
				}
			}
		}.execute();
	}

	public boolean SubmitLog(String version, String serverKey,
							 String hardwareKey, String logXml) {
		return SubmitLog(version, serverKey, hardwareKey, logXml, null);
	}

	public boolean SubmitLog(String version, String serverKey,
							 String hardwareKey, String logXml, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "SubmitLog");
		soapReq.addProperty("version", version);
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("logXml", logXml);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/SubmitLog", soapEnvelope, headers);
			} else {
				httpTransport.call("urn:xmds/SubmitLog", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j
								.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return false;
	}

	public void SubmitStatsAsync(String version, String serverKey,
								 String hardwareKey, String statXml) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		SubmitStatsAsync(version, serverKey, hardwareKey, statXml, null);
	}

	public void SubmitStatsAsync(final String version, final String serverKey,
								 final String hardwareKey, final String statXml,
								 final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return SubmitStats(version, serverKey, hardwareKey, statXml,
						headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("SubmitStats", result);
				}
			}
		}.execute();
	}

	public boolean SubmitStats(String version, String serverKey,
							   String hardwareKey, String statXml) {
		return SubmitStats(version, serverKey, hardwareKey, statXml, null);
	}

	public boolean SubmitStats(String version, String serverKey,
							   String hardwareKey, String statXml, List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "SubmitStats");
		soapReq.addProperty("version", version);
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("statXml", statXml);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/SubmitStats", soapEnvelope,
						headers);
			} else {
				httpTransport.call("urn:xmds/SubmitStats", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j
								.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return false;
	}

	public void MediaInventoryAsync(String version, String serverKey,
									String hardwareKey, String mediaInventory1) throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		MediaInventoryAsync(version, serverKey, hardwareKey, mediaInventory1,
				null);
	}

	public void MediaInventoryAsync(final String version,
									final String serverKey, final String hardwareKey,
									final String mediaInventory1, final List<HeaderProperty> headers)
			throws Exception {

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				return MediaInventory(version, serverKey, hardwareKey,
						mediaInventory1, headers);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("MediaInventory", result);
				}
			}
		}.execute();
	}

	public boolean MediaInventory(String version, String serverKey,
								  String hardwareKey, String mediaInventory1) {
		return MediaInventory(version, serverKey, hardwareKey, mediaInventory1,
				null);
	}

	public boolean MediaInventory(String version, String serverKey,
								  String hardwareKey, String mediaInventory1,
								  List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "MediaInventory");
		soapReq.addProperty("version", version);
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("mediaInventory1", mediaInventory1);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/MediaInventory", soapEnvelope,
						headers);
			} else {
				httpTransport.call("urn:xmds/MediaInventory", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						boolean resultVariable = Boolean.parseBoolean(j
								.toString());
						return resultVariable;
					} else if (obj != null && obj instanceof Boolean) {
						boolean resultVariable = (Boolean) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return false;
	}

	public void GetResourceAsync(String serverKey, String hardwareKey,
								 int layoutId, String regionId, String mediaId, String version)
			throws Exception {
		if (this.eventHandler == null)
			throw new Exception("Async Methods Requires IWsdl2CodeEvents");
		GetResourceAsync(serverKey, hardwareKey, layoutId, regionId, mediaId,
				version, null);
	}

	public void GetResourceAsync(final String serverKey,
								 final String hardwareKey, final int layoutId,
								 final String regionId, final String mediaId, final String version,
								 final List<HeaderProperty> headers) throws Exception {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPreExecute() {
				eventHandler.Wsdl2CodeStartedRequest();
			};

			@Override
			protected String doInBackground(Void... params) {
				return GetResource(serverKey, hardwareKey, layoutId, regionId,
						mediaId, version, headers);
			}

			@Override
			protected void onPostExecute(String result) {
				eventHandler.Wsdl2CodeEndedRequest();
				if (result != null) {
					eventHandler.Wsdl2CodeFinished("GetResource", result);
				}
			}
		}.execute();
	}

	public String GetResource(String serverKey, String hardwareKey,
							  int layoutId, String regionId, String mediaId, String version) {
		return GetResource(serverKey, hardwareKey, layoutId, regionId, mediaId,
				version, null);
	}

	public String GetResource(String serverKey, String hardwareKey,
							  int layoutId, String regionId, String mediaId, String version,
							  List<HeaderProperty> headers) {
		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("urn:xmds", "GetResource");
		soapReq.addProperty("serverKey", serverKey);
		soapReq.addProperty("hardwareKey", hardwareKey);
		soapReq.addProperty("layoutId", layoutId);
		soapReq.addProperty("regionId", regionId);
		soapReq.addProperty("mediaId", mediaId);
		soapReq.addProperty("version", version);
		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		try {
			if (headers != null) {
				httpTransport.call("urn:xmds/GetResource", soapEnvelope,
						headers);

			} else {
				httpTransport.call("urn:xmds/GetResource", soapEnvelope,headerPropertyArrayList);
			}
			Object retObj = soapEnvelope.bodyIn;
			if (retObj instanceof SoapFault) {
				SoapFault fault = (SoapFault) retObj;
				Exception ex = new Exception(fault.faultstring);
				if (eventHandler != null)
					eventHandler.Wsdl2CodeFinishedWithException(ex);
			} else {
				SoapObject result = (SoapObject) retObj;
				if (result.getPropertyCount() > 0) {
					Object obj = result.getProperty(0);
					if (obj != null
							&& obj.getClass().equals(SoapPrimitive.class)) {
						SoapPrimitive j = (SoapPrimitive) obj;
						String resultVariable = j.toString();
						return resultVariable;
					} else if (obj != null && obj instanceof String) {
						String resultVariable = (String) obj;
						return resultVariable;
					}
				}
			}
		} catch (Exception e) {
			if (eventHandler != null)
				eventHandler.Wsdl2CodeFinishedWithException(e);
			e.printStackTrace();
		}
		return "";
	}

}
