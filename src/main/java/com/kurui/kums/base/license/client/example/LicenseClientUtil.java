package com.kurui.kums.base.license.client.example;

import java.io.File;
import java.util.prefs.Preferences;

import com.kurui.kums.base.file.FileUtil;
import com.kurui.kums.base.license.server.KeyStoreUtil;
import com.kurui.kums.base.util.DateUtil;
import com.kurui.kums.base.util.StringUtil;

import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;

/**
 * 客户端 验证授权证书
 */
public class LicenseClientUtil {
	public LicenseClientUtil() {
		// ELTLicenseClient.class.getResourceAsStream(arg0)
	}

	/** The product id of your software */
	public static final String PRODUCT_ID = "cmvp20"; // CUSTOMIZE

	public static final String PRIVATEKEY_SUBJECT = "privatekey"; // CUSTOMIZE
																	// 别名/*此处要和生成lic的subject对应*/
	public static final String PUBSTORE_SUBJECT = "publiccert"; // CUSTOMIZE 别名

	/** The resource name of your private keystore file. */
	// com/chinarewards/gwt/elt/util/
	// \\com\\chinarewards\\gwt\\elt\\util\\
	// /publicCerts.store 需要放于WEB-INF/classes目录
	public static final String KEYSTORE_RESOURCE = "publicCerts.store"; // CUSTOMIZE
				 															// 公匙库文件名
	// ELTLicenseClient.class.getResourceAsStream(arg0)

	/** The password for the keystore. */
	public static final String KEYSTORE_STORE_PWD = "publicstore123"; // CUSTOMIZE
																// 公匙库密码

	/** The password to encrypt the generated license key file. */
	public static final String CIPHER_KEY_PWD = "a8a8a8"; // CUSTOMIZE
															// license文件密码

	protected static final LicenseManager manager = new LicenseManager(
			new DefaultLicenseParam(PRIVATEKEY_SUBJECT,
					Preferences.userNodeForPackage(LicenseClientUtil.class),
					new DefaultKeyStoreParam(
							LicenseClientUtil.class, // CUSTOMIZE
							KEYSTORE_RESOURCE, PUBSTORE_SUBJECT,
							KEYSTORE_STORE_PWD, null),// 这里一定要是null
					new DefaultCipherParam(CIPHER_KEY_PWD)));

	/**
	 * NOTE: This main() method is never called by the actual key server. It is
	 * just useful for debugging the key generator.
	 */
	public static final void main(String args[]) {
		try {
			String licensePath = getCertPath()+ "license.lic";
			licensePath = getCertPath()+ "license201204011145149710.lic";			
			
			manager.install(new java.io.File(licensePath));//加载license
			
			LicenseContent content = manager.verify();//验证

			System.out.println(content.getInfo());			
			System.out.println("授权时间:"+DateUtil.getDateString(content.getIssued(),"yyyy-MM-dd HH:mm:ss"));
			System.out.println("截止时间:"+DateUtil.getDateString(content.getNotAfter(),"yyyy-MM-dd HH:mm:ss"));
			System.out.println(content.getIssuer().getName());
			System.out.println(content.getHolder().getName());
			System.out.println(content.getConsumerAmount());
			
			
//			System.out.println(getCertPath());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String getCertPath() {
		String realPath = "";
		realPath = KeyStoreUtil.class.getResource("").getPath();

		System.out.println(realPath);

		if (!StringUtil.isEmpty(realPath)) {
			String flagstr = "WEB-INF";
			int rootIndex = realPath.indexOf(flagstr);
			
			if (rootIndex < 0) {
				flagstr = "kums-base";
				rootIndex = realPath.indexOf(flagstr);
			}
			
			if (rootIndex < 0) {
				return null;
			} else {
				realPath = realPath.substring(0, rootIndex + flagstr.length());
			}

			int firstIndex = realPath.indexOf("/");
			if (firstIndex == 0) {
				realPath = realPath.substring(1, realPath.length());
			}
			
			realPath=realPath.replaceFirst("file:/","");

			realPath = realPath + File.separator + "license" + File.separator;
		}

		FileUtil.createFolder(realPath);

		System.out.println(realPath);
		return realPath;
	}

}
