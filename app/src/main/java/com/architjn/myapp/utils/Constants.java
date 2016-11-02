package com.architjn.myapp.utils;

import android.content.Context;
import android.os.Environment;

import com.architjn.myapp.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by architjn on 10/10/2016.
 */

public class Constants {

    public static final String PROFILE_THUMB_FOLDER = "ProfileThumb";
    public static int PORT = 5222;
    public static String SERVERNAME = "52.32.181.170";
    public static final String COUNTRY_JSON ="{\n" +
            "  \"countries\": [\n" +
            "    {\n" +
            "      \"code\": \"+7 840\",\n" +
            "      \"name\": \"Abkhazia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+93\",\n" +
            "      \"name\": \"Afghanistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+355\",\n" +
            "      \"name\": \"Albania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+213\",\n" +
            "      \"name\": \"Algeria\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 684\",\n" +
            "      \"name\": \"American Samoa\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+376\",\n" +
            "      \"name\": \"Andorra\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+244\",\n" +
            "      \"name\": \"Angola\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 264\",\n" +
            "      \"name\": \"Anguilla\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 268\",\n" +
            "      \"name\": \"Antigua and Barbuda\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+54\",\n" +
            "      \"name\": \"Argentina\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+374\",\n" +
            "      \"name\": \"Armenia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+297\",\n" +
            "      \"name\": \"Aruba\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+247\",\n" +
            "      \"name\": \"Ascension\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+61\",\n" +
            "      \"name\": \"Australia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+672\",\n" +
            "      \"name\": \"Australian External Territories\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+43\",\n" +
            "      \"name\": \"Austria\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+994\",\n" +
            "      \"name\": \"Azerbaijan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 242\",\n" +
            "      \"name\": \"Bahamas\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+973\",\n" +
            "      \"name\": \"Bahrain\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+880\",\n" +
            "      \"name\": \"Bangladesh\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 246\",\n" +
            "      \"name\": \"Barbados\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 268\",\n" +
            "      \"name\": \"Barbuda\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+375\",\n" +
            "      \"name\": \"Belarus\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+32\",\n" +
            "      \"name\": \"Belgium\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+501\",\n" +
            "      \"name\": \"Belize\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+229\",\n" +
            "      \"name\": \"Benin\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 441\",\n" +
            "      \"name\": \"Bermuda\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+975\",\n" +
            "      \"name\": \"Bhutan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+591\",\n" +
            "      \"name\": \"Bolivia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+387\",\n" +
            "      \"name\": \"Bosnia and Herzegovina\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+267\",\n" +
            "      \"name\": \"Botswana\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+55\",\n" +
            "      \"name\": \"Brazil\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+246\",\n" +
            "      \"name\": \"British Indian Ocean Territory\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 284\",\n" +
            "      \"name\": \"British Virgin Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+673\",\n" +
            "      \"name\": \"Brunei\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+359\",\n" +
            "      \"name\": \"Bulgaria\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+226\",\n" +
            "      \"name\": \"Burkina Faso\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+257\",\n" +
            "      \"name\": \"Burundi\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+855\",\n" +
            "      \"name\": \"Cambodia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+237\",\n" +
            "      \"name\": \"Cameroon\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1\",\n" +
            "      \"name\": \"Canada\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+238\",\n" +
            "      \"name\": \"Cape Verde\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+ 345\",\n" +
            "      \"name\": \"Cayman Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+236\",\n" +
            "      \"name\": \"Central African Republic\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+235\",\n" +
            "      \"name\": \"Chad\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+56\",\n" +
            "      \"name\": \"Chile\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+86\",\n" +
            "      \"name\": \"China\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+61\",\n" +
            "      \"name\": \"Christmas Island\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+61\",\n" +
            "      \"name\": \"Cocos-Keeling Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+57\",\n" +
            "      \"name\": \"Colombia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+269\",\n" +
            "      \"name\": \"Comoros\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+242\",\n" +
            "      \"name\": \"Congo\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+243\",\n" +
            "      \"name\": \"Congo, Dem. Rep. of (Zaire)\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+682\",\n" +
            "      \"name\": \"Cook Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+506\",\n" +
            "      \"name\": \"Costa Rica\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+385\",\n" +
            "      \"name\": \"Croatia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+53\",\n" +
            "      \"name\": \"Cuba\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+599\",\n" +
            "      \"name\": \"Curacao\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+537\",\n" +
            "      \"name\": \"Cyprus\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+420\",\n" +
            "      \"name\": \"Czech Republic\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+45\",\n" +
            "      \"name\": \"Denmark\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+246\",\n" +
            "      \"name\": \"Diego Garcia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+253\",\n" +
            "      \"name\": \"Djibouti\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 767\",\n" +
            "      \"name\": \"Dominica\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 809\",\n" +
            "      \"name\": \"Dominican Republic\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+670\",\n" +
            "      \"name\": \"East Timor\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+56\",\n" +
            "      \"name\": \"Easter Island\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+593\",\n" +
            "      \"name\": \"Ecuador\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+20\",\n" +
            "      \"name\": \"Egypt\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+503\",\n" +
            "      \"name\": \"El Salvador\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+240\",\n" +
            "      \"name\": \"Equatorial Guinea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+291\",\n" +
            "      \"name\": \"Eritrea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+372\",\n" +
            "      \"name\": \"Estonia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+251\",\n" +
            "      \"name\": \"Ethiopia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+500\",\n" +
            "      \"name\": \"Falkland Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+298\",\n" +
            "      \"name\": \"Faroe Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+679\",\n" +
            "      \"name\": \"Fiji\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+358\",\n" +
            "      \"name\": \"Finland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+33\",\n" +
            "      \"name\": \"France\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+596\",\n" +
            "      \"name\": \"French Antilles\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+594\",\n" +
            "      \"name\": \"French Guiana\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+689\",\n" +
            "      \"name\": \"French Polynesia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+241\",\n" +
            "      \"name\": \"Gabon\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+220\",\n" +
            "      \"name\": \"Gambia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+995\",\n" +
            "      \"name\": \"Georgia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+49\",\n" +
            "      \"name\": \"Germany\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+233\",\n" +
            "      \"name\": \"Ghana\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+350\",\n" +
            "      \"name\": \"Gibraltar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+30\",\n" +
            "      \"name\": \"Greece\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+299\",\n" +
            "      \"name\": \"Greenland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 473\",\n" +
            "      \"name\": \"Grenada\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+590\",\n" +
            "      \"name\": \"Guadeloupe\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 671\",\n" +
            "      \"name\": \"Guam\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+502\",\n" +
            "      \"name\": \"Guatemala\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+224\",\n" +
            "      \"name\": \"Guinea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+245\",\n" +
            "      \"name\": \"Guinea-Bissau\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+595\",\n" +
            "      \"name\": \"Guyana\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+509\",\n" +
            "      \"name\": \"Haiti\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+504\",\n" +
            "      \"name\": \"Honduras\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+852\",\n" +
            "      \"name\": \"Hong Kong SAR China\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+36\",\n" +
            "      \"name\": \"Hungary\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+354\",\n" +
            "      \"name\": \"Iceland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+91\",\n" +
            "      \"name\": \"India\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+62\",\n" +
            "      \"name\": \"Indonesia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+98\",\n" +
            "      \"name\": \"Iran\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+964\",\n" +
            "      \"name\": \"Iraq\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+353\",\n" +
            "      \"name\": \"Ireland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+972\",\n" +
            "      \"name\": \"Israel\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+39\",\n" +
            "      \"name\": \"Italy\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+225\",\n" +
            "      \"name\": \"Ivory Coast\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 876\",\n" +
            "      \"name\": \"Jamaica\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+81\",\n" +
            "      \"name\": \"Japan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+962\",\n" +
            "      \"name\": \"Jordan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+7 7\",\n" +
            "      \"name\": \"Kazakhstan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+254\",\n" +
            "      \"name\": \"Kenya\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+686\",\n" +
            "      \"name\": \"Kiribati\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+965\",\n" +
            "      \"name\": \"Kuwait\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+996\",\n" +
            "      \"name\": \"Kyrgyzstan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+856\",\n" +
            "      \"name\": \"Laos\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+371\",\n" +
            "      \"name\": \"Latvia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+961\",\n" +
            "      \"name\": \"Lebanon\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+266\",\n" +
            "      \"name\": \"Lesotho\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+231\",\n" +
            "      \"name\": \"Liberia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+218\",\n" +
            "      \"name\": \"Libya\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+423\",\n" +
            "      \"name\": \"Liechtenstein\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+370\",\n" +
            "      \"name\": \"Lithuania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+352\",\n" +
            "      \"name\": \"Luxembourg\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+853\",\n" +
            "      \"name\": \"Macau SAR China\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+389\",\n" +
            "      \"name\": \"Macedonia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+261\",\n" +
            "      \"name\": \"Madagascar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+265\",\n" +
            "      \"name\": \"Malawi\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+60\",\n" +
            "      \"name\": \"Malaysia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+960\",\n" +
            "      \"name\": \"Maldives\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+223\",\n" +
            "      \"name\": \"Mali\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+356\",\n" +
            "      \"name\": \"Malta\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+692\",\n" +
            "      \"name\": \"Marshall Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+596\",\n" +
            "      \"name\": \"Martinique\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+222\",\n" +
            "      \"name\": \"Mauritania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+230\",\n" +
            "      \"name\": \"Mauritius\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+262\",\n" +
            "      \"name\": \"Mayotte\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+52\",\n" +
            "      \"name\": \"Mexico\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+691\",\n" +
            "      \"name\": \"Micronesia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 808\",\n" +
            "      \"name\": \"Midway Island\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+373\",\n" +
            "      \"name\": \"Moldova\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+377\",\n" +
            "      \"name\": \"Monaco\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+976\",\n" +
            "      \"name\": \"Mongolia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+382\",\n" +
            "      \"name\": \"Montenegro\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1664\",\n" +
            "      \"name\": \"Montserrat\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+212\",\n" +
            "      \"name\": \"Morocco\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+95\",\n" +
            "      \"name\": \"Myanmar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+264\",\n" +
            "      \"name\": \"Namibia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+674\",\n" +
            "      \"name\": \"Nauru\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+977\",\n" +
            "      \"name\": \"Nepal\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+31\",\n" +
            "      \"name\": \"Netherlands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+599\",\n" +
            "      \"name\": \"Netherlands Antilles\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 869\",\n" +
            "      \"name\": \"Nevis\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+687\",\n" +
            "      \"name\": \"New Caledonia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+64\",\n" +
            "      \"name\": \"New Zealand\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+505\",\n" +
            "      \"name\": \"Nicaragua\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+227\",\n" +
            "      \"name\": \"Niger\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+234\",\n" +
            "      \"name\": \"Nigeria\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+683\",\n" +
            "      \"name\": \"Niue\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+672\",\n" +
            "      \"name\": \"Norfolk Island\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+850\",\n" +
            "      \"name\": \"North Korea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 670\",\n" +
            "      \"name\": \"Northern Mariana Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+47\",\n" +
            "      \"name\": \"Norway\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+968\",\n" +
            "      \"name\": \"Oman\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+92\",\n" +
            "      \"name\": \"Pakistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+680\",\n" +
            "      \"name\": \"Palau\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+970\",\n" +
            "      \"name\": \"Palestinian Territory\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+507\",\n" +
            "      \"name\": \"Panama\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+675\",\n" +
            "      \"name\": \"Papua New Guinea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+595\",\n" +
            "      \"name\": \"Paraguay\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+51\",\n" +
            "      \"name\": \"Peru\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+63\",\n" +
            "      \"name\": \"Philippines\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+48\",\n" +
            "      \"name\": \"Poland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+351\",\n" +
            "      \"name\": \"Portugal\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 787\",\n" +
            "      \"name\": \"Puerto Rico\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+974\",\n" +
            "      \"name\": \"Qatar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+262\",\n" +
            "      \"name\": \"Reunion\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+40\",\n" +
            "      \"name\": \"Romania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+7\",\n" +
            "      \"name\": \"Russia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+250\",\n" +
            "      \"name\": \"Rwanda\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+685\",\n" +
            "      \"name\": \"Samoa\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+378\",\n" +
            "      \"name\": \"San Marino\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+966\",\n" +
            "      \"name\": \"Saudi Arabia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+221\",\n" +
            "      \"name\": \"Senegal\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+381\",\n" +
            "      \"name\": \"Serbia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+248\",\n" +
            "      \"name\": \"Seychelles\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+232\",\n" +
            "      \"name\": \"Sierra Leone\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+65\",\n" +
            "      \"name\": \"Singapore\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+421\",\n" +
            "      \"name\": \"Slovakia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+386\",\n" +
            "      \"name\": \"Slovenia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+677\",\n" +
            "      \"name\": \"Solomon Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+27\",\n" +
            "      \"name\": \"South Africa\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+500\",\n" +
            "      \"name\": \"South Georgia and the South Sandwich Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+82\",\n" +
            "      \"name\": \"South Korea\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+34\",\n" +
            "      \"name\": \"Spain\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+94\",\n" +
            "      \"name\": \"Sri Lanka\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+249\",\n" +
            "      \"name\": \"Sudan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+597\",\n" +
            "      \"name\": \"Suriname\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+268\",\n" +
            "      \"name\": \"Swaziland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+46\",\n" +
            "      \"name\": \"Sweden\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+41\",\n" +
            "      \"name\": \"Switzerland\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+963\",\n" +
            "      \"name\": \"Syria\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+886\",\n" +
            "      \"name\": \"Taiwan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+992\",\n" +
            "      \"name\": \"Tajikistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+255\",\n" +
            "      \"name\": \"Tanzania\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+66\",\n" +
            "      \"name\": \"Thailand\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+670\",\n" +
            "      \"name\": \"Timor Leste\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+228\",\n" +
            "      \"name\": \"Togo\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+690\",\n" +
            "      \"name\": \"Tokelau\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+676\",\n" +
            "      \"name\": \"Tonga\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 868\",\n" +
            "      \"name\": \"Trinidad and Tobago\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+216\",\n" +
            "      \"name\": \"Tunisia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+90\",\n" +
            "      \"name\": \"Turkey\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+993\",\n" +
            "      \"name\": \"Turkmenistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 649\",\n" +
            "      \"name\": \"Turks and Caicos Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+688\",\n" +
            "      \"name\": \"Tuvalu\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 340\",\n" +
            "      \"name\": \"U.S. Virgin Islands\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+256\",\n" +
            "      \"name\": \"Uganda\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+380\",\n" +
            "      \"name\": \"Ukraine\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+971\",\n" +
            "      \"name\": \"United Arab Emirates\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+44\",\n" +
            "      \"name\": \"United Kingdom\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1\",\n" +
            "      \"name\": \"United States\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+598\",\n" +
            "      \"name\": \"Uruguay\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+998\",\n" +
            "      \"name\": \"Uzbekistan\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+678\",\n" +
            "      \"name\": \"Vanuatu\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+58\",\n" +
            "      \"name\": \"Venezuela\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+84\",\n" +
            "      \"name\": \"Vietnam\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+1 808\",\n" +
            "      \"name\": \"Wake Island\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+681\",\n" +
            "      \"name\": \"Wallis and Futuna\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+967\",\n" +
            "      \"name\": \"Yemen\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+260\",\n" +
            "      \"name\": \"Zambia\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+255\",\n" +
            "      \"name\": \"Zanzibar\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"code\": \"+263\",\n" +
            "      \"name\": \"Zimbabwe\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static String getProfileThumbFolder(Context context) {
        String path = Environment.getExternalStorageDirectory() +
                File.separator + context.getString(R.string.app_name) + File.separator +
                Constants.PROFILE_THUMB_FOLDER;
        try {
            File f = new File(path);
            if (!f.exists())
                f.mkdirs();
            File nom = new File(path + File.separator + ".nomedia");
            if (!nom.exists())
                nom.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
//    public static String SERVERNAME = "107.180.68.152";

}
