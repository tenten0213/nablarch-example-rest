package com.nablarch.example.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;

import nablarch.core.util.ObjectUtil;
import nablarch.core.util.StringUtil;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

/**
 * MultivaluedMapユーティリティ
 *
 * @author TIS
 */
public final class MultivaluedMapUtil {

    /**
     * 隠蔽コンストラクタ
     */
    private MultivaluedMapUtil() {
    }


    /**
     * オブジェクトから階層構造をキーで表現したマップを作成します。
     *
     *
     * @param bean Bean
     * @return マップ
     */
    public static MultivaluedMap<String, String> convertMultiValuedMap(Object bean) {
        return convertMultiValuedMap("", bean);
    }

    /**
     * オブジェクトから階層構造をキーで表現したマップを作成します。
     * <p/>
     * 本メソッドではオブジェクト内の全てのゲッターメソッドを対象として、データ取得を行い、
     * 以下の規則に従って、取得したデータをマップに格納します。<br/>
     * プロパティ名はゲッターメソッド名からgetを除き先頭を大文字に変換した名称とします。
     *
     * <table border="1">
     * <tr bgcolor="#cccccc">
     *   <th>対象データ型</th>
     *   <th>格納キー</th>
     *   <th>格納データ型</th>
     *   <th>格納データ内容</th>
     *   <th>備考</th>
     * </tr>
     * <tr>
     *   <td>String</td>
     *   <td>"${プリフィックス}." + プロパティ名</td>
     *   <td>String</td>
     *   <td>取得データそのまま</td>
     *   <td>&nbsp</td>
     * </tr>
     * <tr>
     *   <td>String[]</td>
     *   <td>"${プリフィックス}." + プロパティ名</td>
     *   <td>String[]</td>
     *   <td>取得データそのまま</td>
     *   <td>&nbsp</td>
     * </tr>
     * <tr>
     *   <td>Number</td>
     *   <td>"${プリフィックス}." + プロパティ名</td>
     *   <td>String</td>
     *   <td>取得データを文字列化したもの</td>
     *   <td>&nbsp</td>
     * </tr>
     * <tr>
     *   <td>Boolean</td>
     *   <td>"${プリフィックス}." + プロパティ名</td>
     *   <td>String</td>
     *   <td>取得データを文字列化したもの</td>
     *   <td>&nbsp</td>
     * </tr>
     * <tr>
     *   <td>その他オブジェクト</td>
     *   <td>"${プリフィックス}." + プロパティ名  + "." + オブジェクト内のプロパティ名</td>
     *   <td>StringまたはString[]</td>
     *   <td>オブジェクト内のプロパティデータ型による</td>
     *   <td>再帰的に処理が行われる</td>
     * </tr>
     * <tr>
     *   <td>その他オブジェクトの配列</td>
     *   <td>"${プリフィックス}." + プロパティ名  + "[${要素番号}]" + "." + オブジェクト内のプロパティ名</td>
     *   <td>StringまたはString[]</td>
     *   <td>オブジェクト内のプロパティデータ型による</td>
     *   <td>再帰的に処理が行われる</td>
     * </tr>
     * </table>
     *
     * @param prefix プリフィックス
     * @param bean Bean
     * @return マップ
     */
    public static MultivaluedMap<String, String> convertMultiValuedMap(String prefix, Object bean) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl<>();

        if (bean == null) {
            // 空のマップを返却
            return map;
        }

        // 内部で使用するプリフィックスの調整
        String innerPrefix;
        if (StringUtil.isNullOrEmpty(prefix)) {
            innerPrefix = "";
        } else {
            innerPrefix = prefix + '.';
        }

        // Mapの場合は値をつめなおして返却
        if (bean instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> e : ((Map<?, ?>) bean).entrySet()) {
                if (e.getKey() != null) {
                    if (e.getValue() instanceof Map<?, ?>) {
                        map.putAll(convertMultiValuedMap(innerPrefix + e.getKey(), e.getValue()));
                    } else {
                        // キャスト失敗の処理入れたほうが良いかも
                        if (e.getValue() instanceof ArrayList) {
                            map.putSingle(innerPrefix + e.getKey(), (String) ((ArrayList)e.getValue()).get(0));
                        }
                    }
                }
            }
            return map;
        }

        // 全てのゲッターメソッドを走査しマップに格納
        for (Method m : ObjectUtil.getGetterMethods(bean.getClass())) {
            String propName = ObjectUtil.getPropertyNameFromGetter(m);
            Object o = ObjectUtil.getProperty(bean, propName);

            if (o == null) {
                map.putSingle(innerPrefix + propName, null);

            } else if (o instanceof String) {
                map.putSingle(innerPrefix + propName, (String) o);

            } else if (o instanceof Number) {
                map.putSingle(innerPrefix + propName, StringUtil.toString(o));

            } else if (o instanceof Boolean) {
                map.putSingle(innerPrefix + propName, o.toString());

            } else if (m.getReturnType().isArray()) {
                int length = Array.getLength(o);
                for (int i = 0; i < length; i++) {
                    // map.putAll(convertMultiValuedMap(innerPrefix + propName + "[" + i + "]", Array.get(o, i)));
                    map.putAll(convertMultiValuedMap(innerPrefix + propName + i, Array.get(o, i)));
                }

            } else {
                map.putAll(convertMultiValuedMap(innerPrefix + propName, o));
            }
        }
        return map;
    }
    
    public static Map<String, String> convertFlatMap(MultivaluedMap<String, String> mmap) {
        Map<String, String> flatMap = new HashMap<>();
        for(Map.Entry<String, List<String>> entry : mmap.entrySet()) {
            if (entry.getValue().size() == 1) {
                flatMap.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return flatMap;
    }

}
