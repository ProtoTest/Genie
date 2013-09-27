/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prototest.TG_WEB.ui;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.jsoup.Connection;

/**
 * Example program to list links from a URL.
 */
public class GetForms {
    
 
    public  static List<List<String>> radioOptions(Elements radios){
        
        Set<String> radioNameSet= Sets.newHashSet();
        
        for (Element link : radios) {
             radioNameSet.add(link.attr("name").toString());
        }

        List<Elements> radioGroups = Lists.newArrayList();
        
        for(String name : radioNameSet) {             
        radioGroups.add(radios.select("[name="+name+"]"));
        }
        
        List<Set<String>> sets = new ArrayList<Set<String>>();   
        
        for(Elements sub_radio: radioGroups){
            
        List<String> subSet = Lists.newArrayList();

        for (Element link : sub_radio) {
        subSet.add(link.attr("id"));
        }
        
        sets.add(new HashSet(subSet));
        
        } 
                
        Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
        List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);
                        
        return cartesianList;
    }

    
    public static List<List<String>> selectionOptions(Elements selects){
        List<Set<String>> sets = new ArrayList<Set<String>>();   
        for (Element link : selects) {
            
        List<String> subSet = Lists.newArrayList();            
        Elements optionGroups = link.getElementsByTag("option");
        for (Element opt : optionGroups ) {
            subSet.add("<id>=" + link.attr("id") +"</id> <lable>=" + opt.attr("value") + "</lable>");            
        }   
        
        sets.add(new HashSet(subSet));
        
        } 
        Set<List<String>> cartesianSet = com.google.common.collect.Sets.cartesianProduct(sets);
        List<List<String>> cartesianList = Lists.newArrayList(cartesianSet);
        return cartesianList;        
    }

    public static List<String> areasList(Elements areas){
        List<String> areaIDs = Lists.newArrayList();

        for (Element link : areas) {
            if(!link.attr("type").equals("hidden"))
             areaIDs.add(link.attr("id")); 
        }
        
        return areaIDs;        
    }
    
    
    public static List<String> buttonsList(Elements buttons){
        List<String> buttonIDs = Lists.newArrayList();
        for (Element link : buttons) {
          buttonIDs.add(link.attr("id")); 
        }        
        return buttonIDs;        
    }
    
    public static List<String> inputsList(Elements inputs){
        
        List<String> inputsID = Lists.newArrayList();
        
        for (Element link : inputs) {
            
            if(! ( link.attr("id").isEmpty() || link.attr("type").equals("hidden") || link.attr("type").equals("file") )){

            // catch email input
            if (link.attr("id").contains("email")  || link.attr("type").equals("email") ||
                link.attr("name").contains("email")|| link.attr("class").contains("email")) {                
                inputsID.add("<type=email><id=" + link.attr("id") + "</id>");                
            } else                  
            // catch password input
            if (link.attr("type").equals("password")) { // catch text input

                inputsID.add("<type=password><id=" + link.attr("id") + "</id>");                

             } else {
                inputsID.add("<type=text><id=" + link.attr("id") + "</id>");                
            }

            } // input elements we need

        } // end for loop
      return inputsID;   
      
    }
    
    
    public static void main(String[] args) throws IOException {
        //String url = "http://www.surveymonkey.com/s/customer-satisfaction-survey-template";
        //String url = "http://help.change.org/anonymous_requests/new";
        //String url = "https://mail.yahoo.com";
        //String url = "https://www.google.com";
//        String url = "http://prototest.com/contact/";
//
//        print("Fetching %s...", url);
//
//       Connection connection = Jsoup.connect(url);
//                                connection.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/536.30.1 (KHTML, like Gecko) Version/6.0.5 Safari/536.30.1");
//				Connection.Response response = connection.execute();
//                                
//                
//        if (response.statusCode() >= 200 && response.statusCode() < 300) {
//           Document doc = response.parse();
//
//        Elements forms = doc.select("form");
//        Elements inputs = doc.select("input[type=text], input[type=email], input[password]");
//        Elements areas = doc.select("textarea");
//        Elements selects = doc.select("select");
//        Elements buttons = doc.select("input[type=button], input[type=submit], input[type=reset]");
//        Elements radios = doc.select("input[type=radio]");
//        
//        int i =0;
//        for(Element f: forms){
//            if((f.attr("id").equals(f.attr("name"))) && (f.hasAttr("action"))
//                && (f.hasAttr("method")) && (f.attr("target").isEmpty()) ){
//                i++;
//                System.out.println(f.toString());
//                
//            }
//        }
//        
//    System.out.println(i);            
//
//        // Get radio cartesian product carcombinations
//        List<List<String>> radioGroups = radioOptions(radios);
//
//        // Get selection cartesian product carcombinations
//        List<List<String>> selectionGroups = selectionOptions(selects);
//        
//        // Get list of buttons
//        List<String> buttonsGroup = buttonsList(buttons);
//        
//        // Get list of areas
//        List<String> areasGroup = areasList(areas);
//        
//        // Get list of inputs
//        List<String> inputsGroup = inputsList(inputs);
         int i = "<type=email><id>12345</id>".indexOf("<id>") + 4;
         int j = "<type=email><id>12345</id>".indexOf("</id>");
         
            System.out.println("<type=email><id>12345</id>".substring(i, j));
            
//        if (!selectionGroups.isEmpty()){
//             System.out.println(selectionGroups.size());
//             Random generator = new Random();
//             List<String> element;
//             int s; 
//             int i = 0;
//             int j = (int) (selectionGroups.size() * 0.1);             
//             System.out.println(j + " times");
//
//             do{
//             s = generator.nextInt(selectionGroups.size()-1);
//             System.out.println(s);
//             element = selectionGroups.get(s); 
//             for(String e: element){
//             System.out.println(e);                 
//             }
//             i++;
//             } while (i<j);
//        }
        
//        if (!cartesianList.isEmpty()){
//             System.out.println(cartesianList.size());
//             Random generator = new Random();
//             List<String> element;
//             int s; 
//             int i = 0;
//             int j = (int) (cartesianList.size() * 0.0001);             
//             System.out.println(j + " times");
//
//             do{
//             s = generator.nextInt(cartesianList.size()-1);
//             element = cartesianList.get(s);       
//             System.out.println(s);
//             System.out.println(element);
//             i++;
//             } while (i<j);
//        }
                        
//     } // end of status code >= 200 <=300
                                
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
