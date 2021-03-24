package com.officego.commonlib.common.alipay;

/**
 * Created by shijie
 * Date 2021/3/15
 **/
public class AlipayConfig {
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCM53RihcIz4IVtaDzzu4PwCsv0JFoUUXB792X8p+IgXugdmJju7DSHIfOLOTqk11eJzaKOOX2EXnBaud/nqMcWkfp5wUt6hyvgLSWxyDBDbwVT5+feVuH6XPBfhADhpjXlZV/4+lAIctxse4pUyR6xowbvURQdU1CrVgQ+Fg1+jhxFMTQb4Qujuk2tgDCREwau51nMbuXunpbwVTp2zqprgeali7rICx1fapyR6WVJLD2O2arMqlhm464L/VOOghiBRdrYSbu3E8NY/CbDwwxXcp8k8c59WLbTTf19HEQqVqSmRKShWLEmfyCY6TaKAJItMz+3b85agebRtnWfzKHTAgMBAAECggEANV+jZYKXasE8qZHXrNe2BaKBWWy/tnyW6E5xkoAhlvN8Z2GbZL2StDF3KKC4ByMPJMMBNXCvjAmd86//EaiMKY+IWsCOj85OH2/anHphVdnjIcJxwXYVyZmU8Al3sAIe3lNFRHHFCT5BItK1NqKsfstrYBHNVcJuh0hg+QeveuVTYaofwNsL+znv1r/VKzv6gQoKmqzhMVS1fL/UmUE3Cg3V21l09mW4N58BI+vppomJRrCVGk7agXeXK0wfGpnd9nV9Jb6AcMsjEjBD06f1MxB3aPf+PKaNEPLAsVUuIVuJY5APDDdBmvldiCz1D5raA1F0diulQBe89P8QzgoxwQKBgQD00H+iKJR3pKfxBWuHggVCG/7MNZX50japzedjMX+mFDIhR46STs/Ciheie6cNnC8AsjIf7Mwyac7GyLhNPqgdCXS/vbuO1mVnWYqQcx3mnwQbX+pI6Ts63i59D4Qu3Q5Ymyls9ZtEvwcZVnTGePeH8h1ClzrTaq1JO5zenWCi4QKBgQCTV452j7oLK+ZOv6hiHt8D3q/hiIoToqlMEFFNsesj8fRVb9DAKDERNgoyKlYgK8FGORrLKWorTm75R4FDqXb1eSm+QBsMVFDkICkqHtGgEwRy8iwGWbrR9LuprQvX0Km/84nR1c9+A9fYDh2OHuqaMo2KSw2QDcGxM+1/PSEPMwKBgQCmgrV89rrckYqEnVogxaeL6nyoKn5Cd98kKV8LEZyvuibKieAH1UDsIX6nzhC2ahdyEsGTAz1zwJGalOT97PLtJQRacdMC+QHphHaBfzrEJD3n3Jn+U1ws0vetqgMO2N2sGITZjke4P3G1ox8z6wz5UwzhwPiK2ZDZMYmlxEb5IQKBgCAP3HNWQUCvE9WtBQIc/6CC87jbEljFe5uOrXJ/tdBDlUyGIWG/HTZGkR9rcQRdIRib0oXFBFJaJfHeqnVDyhwOepPotToQmu2ZRfvhavIvUTeGFShonCuV2sRIYJxczqNzZlkf8p/UTEK6h5nmhlx5ha1DObdQFwbtas/WSHxbAoGAO7dzxaCMJHayuWD5PjngrPDB62yRPfpZ9+KFI4H77dYEId8SwnyfL9SBUzvbNHBHHKWtGJ1ewx4VzI9UKsJra/4DdKVkTnmbF9HR7YkCfn7/4w3hUE9a3rw8DTJ5BfYrdz+9mds8WVqHBaM+T5FzI8UudS2rYn61Z7ELpEbAFyQ=";
    public static final String RSA_PRIVATE = "";

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
}
