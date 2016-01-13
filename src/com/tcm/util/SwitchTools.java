package com.tcm.util;

/**
 * 名字转换模块
 * Created by azurexsyl on 2015/12/16.
 */
public class SwitchTools {

    /***
     * 把URI转换为简称
     * @param uri
     * @return
     */
    public static String getURIDomain(String uri) {
        if(uri.contains("relation#")) {
            return "relation";
        }else if(uri.contains("property#")) {
            return "property";
        } if(uri.contains("http://zcy.ckcest.cn/tcm/med")) {
            return  "med";
        } else if(uri.contains("http://zcy.ckcest.cn/tcm/pre")) {
            return "pre";
        } else if(uri.contains("http://zcy.ckcest.cn/tcm/dis")) {
            return "dis";
        } else if(uri.contains("http://zcy.ckcest.cn/tcm/syn")) {
            return "syn";
        } else if(uri.contains("http://zcy.ckcest.cn/tcm/chem")) {
            return "chem";
        } else if(uri.contains("http://zcy.ckcest.cn/tcm/ori")) {
            return "ori";
        }
        return null;
    }

    public static boolean isSameDomain(String uri1, String uri2) {
        if(uri1.contains("http://zcy.ckcest.cn/tcm/med") && uri2.contains("http://zcy.ckcest.cn/tcm/med")) {
            return true;
        } else if (uri1.contains("http://zcy.ckcest.cn/tcm/pre") && uri2.contains("http://zcy.ckcest.cn/tcm/pre")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/dis/tcm") && uri2.contains("http://zcy.ckcest.cn/tcm/dis/tcm")) {
            return true;
        }
        else if (uri1.contains("http://zcy.ckcest.cn/tcm/dis/wm") && uri2.contains("http://zcy.ckcest.cn/tcm/dis/wm")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/syn") && uri2.contains("http://zcy.ckcest.cn/tcm/syn")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/chem") && uri2.contains("http://zcy.ckcest.cn/tcm/chem")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/ori/plant") && uri2.contains("http://zcy.ckcest.cn/tcm/ori/plant")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/ori/animal") && uri2.contains("http://zcy.ckcest.cn/tcm/ori/animal")) {
            return true;
        }else if (uri1.contains("http://zcy.ckcest.cn/tcm/ori/mineral") && uri2.contains("http://zcy.ckcest.cn/tcm/ori/mineral")) {
            return true;
        }
        return false;

    }
}
