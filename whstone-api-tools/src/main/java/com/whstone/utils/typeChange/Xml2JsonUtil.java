package com.whstone.utils.typeChange;


import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Xml2JsonUtil {
    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param xml xml格式的字符串
     * @return 成功返回json 格式的字符串;失败反回null
     */
    @SuppressWarnings("unchecked")
    public static String xml2JSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(is);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换一个xml格式的字符串到json格式
     *
     * @param file java.io.File实例是一个有效的xml文件
     * @return 成功反回json 格式的字符串;失败反回null
     */
    @SuppressWarnings("unchecked")
    public static String xml2JSON(File file) {
        JSONObject obj = new JSONObject();
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(file);
            Element root = doc.getRootElement();
            obj.put(root.getName(), iterateElement(root));
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 一个迭代方法
     *
     * @param element : org.jdom.Element
     * @return java.util.Map 实例
     */
    @SuppressWarnings("unchecked")
    private static Map iterateElement(Element element) {
        List jiedian = element.getChildren();
        Element et = null;
        Map obj = new HashMap();
        List list = null;
        for (int i = 0; i < jiedian.size(); i++) {
            list = new LinkedList();
            et = (Element) jiedian.get(i);
            if (et.getTextTrim().equals("")) {
                if (et.getChildren().size() == 0)
                    continue;
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(iterateElement(et));
                obj.put(et.getName(), list);
            } else {
                if (obj.containsKey(et.getName())) {
                    list = (List) obj.get(et.getName());
                }
                list.add(et.getTextTrim());
                obj.put(et.getName(), list);
            }
        }
        return obj;
    }

    public static String xmltoJson(String xml) {
        String ss = "";
        try {
            XMLSerializer xmlSerializer = new XMLSerializer();
            ss = xmlSerializer.read(xml).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ss;
    }

    // 测试
    public static void main(String[] args) {
        TypeChangeUtils ty = new TypeChangeUtils();
        String ss = ty.getJSONStringFromXml("<listtemplatesresponse cloud-stack-version='4.5.0.0'><count>7</count><template><id>2d102467-4f19-42aa-ada6-cc578d128b07</id><name>winxp</name><displaytext>winxp</displaytext><ispublic>true</ispublic><created>2015-08-17T16:16:04+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28758, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>5fceae60-8156-4c6e-ac61-9b1469cdeeab</id><name>vdit-win7</name><displaytext>vdit-win7</displaytext><ispublic>true</ispublic><created>2015-06-11T19:40:28+0800</created><isready>true</isready><passwordenabled>true</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ada17-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (64-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>19327352832</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>6ee50a9b-71d3-47be-9450-954e8d63d3c2</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;viridian_reference_tsc:true;viridian_time_ref_count:true;pae:true;device_id:0002;nx:true;timeoffset:28800, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver61}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>d9ccdef2-9b3a-4848-ab4e-ef477c420c41</id><name>winxpb</name><displaytext>winxpb</displaytext><ispublic>true</ispublic><created>2015-08-25T14:56:53+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>2d102467-4f19-42aa-ada6-cc578d128b07</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28763, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>2769368d-2115-42a7-8329-3698b5624b49</id><name>winxpapp</name><displaytext>winxpapp</displaytext><ispublic>true</ispublic><created>2015-08-11T10:52:12+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ac4c2-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28742, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>0ed584d5-d443-45f2-8a71-65b87fb5099b</id><name>winxp-a</name><displaytext>winxp-a</displaytext><ispublic>true</ispublic><created>2015-08-18T10:46:42+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28756, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>4ba9f6db-7f39-409f-8554-852df71c27c4</id><name>win7app2</name><displaytext>win7app2</displaytext><ispublic>true</ispublic><created>2015-07-23T10:10:21+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ada17-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (64-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>21474836480</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>fe56238c-13f7-4bfa-a83c-9c6bf26517c1</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;viridian_reference_tsc:true;viridian_time_ref_count:true;pae:true;device_id:0002;nx:true;timeoffset:28800, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver61}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>a906309e-0345-11e5-b697-00505684737f</id><name>CentOS 5.6(64-bit) no GUI (XenServer)</name><displaytext>CentOS 5.6(64-bit) no GUI (XenServer)</displaytext><ispublic>true</ispublic><created>2015-06-17T10:25:46+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>true</crossZones><ostypeid>a9114df2-0345-11e5-b697-00505684737f</ostypeid><ostypename>CentOS 5.6 (64-bit)</ostypename><account>system</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>21474836480</size><templatetype>BUILTIN</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>true</isextractable><checksum>905cec879afd9c9d22ecc8036131a180</checksum><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>true</isdynamicallyscalable></template></listtemplatesresponse>");
        System.out.println(ss);


        String xml2json = xml2JSON("<listtemplatesresponse cloud-stack-version='4.5.0.0'><count>7</count><template><id>2d102467-4f19-42aa-ada6-cc578d128b07</id><name>winxp</name><displaytext>winxp</displaytext><ispublic>true</ispublic><created>2015-08-17T16:16:04+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28758, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>5fceae60-8156-4c6e-ac61-9b1469cdeeab</id><name>vdit-win7</name><displaytext>vdit-win7</displaytext><ispublic>true</ispublic><created>2015-06-11T19:40:28+0800</created><isready>true</isready><passwordenabled>true</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ada17-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (64-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>19327352832</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>6ee50a9b-71d3-47be-9450-954e8d63d3c2</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;viridian_reference_tsc:true;viridian_time_ref_count:true;pae:true;device_id:0002;nx:true;timeoffset:28800, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver61}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>d9ccdef2-9b3a-4848-ab4e-ef477c420c41</id><name>winxpb</name><displaytext>winxpb</displaytext><ispublic>true</ispublic><created>2015-08-25T14:56:53+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>2d102467-4f19-42aa-ada6-cc578d128b07</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28763, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>2769368d-2115-42a7-8329-3698b5624b49</id><name>winxpapp</name><displaytext>winxpapp</displaytext><ispublic>true</ispublic><created>2015-08-11T10:52:12+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ac4c2-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28742, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>0ed584d5-d443-45f2-8a71-65b87fb5099b</id><name>winxp-a</name><displaytext>winxp-a</displaytext><ispublic>true</ispublic><created>2015-08-18T10:46:42+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90dcfbd-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows XP (32-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>10737418240</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>f8c650aa-2016-465e-9dee-0798ad3c16a3</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;pae:true;nx:true;timeoffset:28756, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver56}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>4ba9f6db-7f39-409f-8554-852df71c27c4</id><name>win7app2</name><displaytext>win7app2</displaytext><ispublic>true</ispublic><created>2015-07-23T10:10:21+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>false</crossZones><ostypeid>a90ada17-0345-11e5-b697-00505684737f</ostypeid><ostypename>Windows 7 (64-bit)</ostypename><account>admin</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>21474836480</size><templatetype>USER</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>false</isextractable><sourcetemplateid>fe56238c-13f7-4bfa-a83c-9c6bf26517c1</sourcetemplateid><details>{platform=viridian:true;acpi:1;apic:true;viridian_reference_tsc:true;viridian_time_ref_count:true;pae:true;device_id:0002;nx:true;timeoffset:28800, keyboard=us, Message.ReservedCapacityFreed.Flag=false, hypervisortoolsversion=xenserver61}</details><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>false</isdynamicallyscalable></template><template><id>a906309e-0345-11e5-b697-00505684737f</id><name>CentOS 5.6(64-bit) no GUI (XenServer)</name><displaytext>CentOS 5.6(64-bit) no GUI (XenServer)</displaytext><ispublic>true</ispublic><created>2015-06-17T10:25:46+0800</created><isready>true</isready><passwordenabled>false</passwordenabled><format>VHD</format><isfeatured>true</isfeatured><crossZones>true</crossZones><ostypeid>a9114df2-0345-11e5-b697-00505684737f</ostypeid><ostypename>CentOS 5.6 (64-bit)</ostypename><account>system</account><zoneid>c6cc1565-7f87-4e31-b5bf-7220b348ec3f</zoneid><zonename>local</zonename><status>Download Complete</status><size>21474836480</size><templatetype>BUILTIN</templatetype><hypervisor>XenServer</hypervisor><domain>ROOT</domain><domainid>a903d47d-0345-11e5-b697-00505684737f</domainid><isextractable>true</isextractable><checksum>905cec879afd9c9d22ecc8036131a180</checksum><sshkeyenabled>false</sshkeyenabled><isdynamicallyscalable>true</isdynamicallyscalable></template></listtemplatesresponse>");

        System.out.println("xml2json=" + xml2json);
    }
}