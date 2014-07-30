package com.dewav.intexzone2;

import android.text.format.Time;
import java.io.File;
import java.io.FileOutputStream;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.transform.stream.StreamResult;
import java.io.Writer;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStream;

public class LogFileParase
{
  private static final String LOG_FILE_NAME = "charging_log_details.xml";
  private static final String LOG_FILE_PATH = "data/data/com.dewav.intexzone2";
  private static final int LOG_MAX_NUM = 10;

  private String formatMonth(int paramInt)
  {
    switch (paramInt)
    {
    default:
      return "Jan.";
    case 0:
      return "Jan.";
    case 1:
      return "Feb.";
    case 2:
      return "Mar.";
    case 3:
      return "Apr.";
    case 4:
      return "May.";
    case 5:
      return "Jun.";
    case 6:
      return "Jul.";
    case 7:
      return "Aug.";
    case 8:
      return "Sep.";
    case 9:
      return "Oct.";
    case 10:
      return "Nov.";
    case 11:
      return "Dec.";
    }
  }

  public static String formatTime(Time paramTime)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramTime.hour;
    if (i < 10){
      localStringBuilder.append(0);
    }else{
      localStringBuilder.append(i);
    }
    localStringBuilder.append(":");
    
    int j = paramTime.minute;
    if (j < 10){
      localStringBuilder.append(0);
    }else{
      localStringBuilder.append(j);
    }
    
    return localStringBuilder.toString();
  }
  
public List readLogFile() {
        ArrayList<ChargingInfo> infoList = new ArrayList<ChargingInfo>();
        File path = new File("data/data/com.dewav.intexzone2");
        File file = new File("data/data/com.dewav.intexzone2/charging_log_details.xml");
     try{
        FileInputStream inputStream = new FileInputStream(file); 
       
        if(path.exists()) {
            if(file.exists()) {
                FileInputStream localFileInputStream1 = inputStream;//new FileInputStream(file);
            }
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);
                Element root = doc.getDocumentElement();
                NodeList pNodes = root.getElementsByTagName("info");
                for(int i = 0; i < pNodes.getLength(); i = i + 1) {
                    ChargingInfo info = new ChargingInfo();
                    Element pNode = (Element)pNodes.item(i);
                    NodeList cNodes = pNode.getChildNodes();
                    for(int j = 0; j < cNodes.getLength(); j = j + 1) {
                        Node node = cNodes.item(j);
                        //if(node.getNodeType() == 1) {
                        if(node.getNodeType() == Node.ELEMENT_NODE) {
                            Node cNode = (Element)node;
                            if("level".equals(cNode.getNodeName())) {
                            //	String localint2 = (String)cNode.getNodeName();
								info.setChargingSaturationLevel(Integer.valueOf(
										                 node.getFirstChild()
														.getNodeValue()).intValue());   
                            } else if("duration".equals(cNode.getNodeName())) {
                            //	string localint3 = j;
                                info.setDuration(Integer.valueOf(
                                		node.getFirstChild().getNodeValue()).intValue());                
                            } else if("time".equals(cNode.getNodeName())) {
                            //	Node localString4 = cNodes.item(j);
                                info.setChargingEndTime(String.valueOf(node
									   .getFirstChild().getNodeValue()));  
                            }
                        }
                    }
                    infoList.add(info);
                }
                inputStream.close();
                
                return infoList;
            } catch(Exception localException1) {
            	//localException1.printStackTrace();
            }
        }
     }catch(FileNotFoundException e){ //(Exception localException5) {
     	//e.printStackTrace();
     }
        return infoList;
}
  private String getCurrentTime()
  {
    Time localTime = new Time();
    localTime.setToNow();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(localTime.monthDay);
    localStringBuilder.append(", ");
    localStringBuilder.append(formatMonth(localTime.month));
    localStringBuilder.append(", ");
    localStringBuilder.append(formatTime(localTime));
    return localStringBuilder.toString();
  }

  
  public void InsertLog(boolean paramBoolean, int paramInt1, int paramInt2)
  {
      File path = new File("data/data/com.dewav.intexzone2");
      File file = new File("data/data/com.dewav.intexzone2/charging_log_details.xml");
      try {
        FileOutputStream outputStream = new FileOutputStream(file);
        
        Document doc = null;
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      if(paramBoolean) {
          doc = builder.newDocument();
      } else {
          try {
              doc = builder.parse(file);
     // }
          Element infoEle = doc.createElement("info");
          Element level = doc.createElement("level");
          
         // int chargingLevel = ((ChargingInfo)(this.readLogFile())).getChargingSaturationLevel();
          level.appendChild(doc.createTextNode(String.valueOf(paramInt1)));   
          
          infoEle.appendChild(level);
          Element duration = doc.createElement("duration");
          
          //int chargingDuration = ((ChargingInfo)(this.readLogFile())).getduration();
          duration.appendChild(doc.createTextNode(String.valueOf(paramInt2))); 
          
          infoEle.appendChild(duration);
          Element time = doc.createElement("time");
          time.appendChild(doc.createTextNode(getCurrentTime()));
          infoEle.appendChild(time);
          if(paramBoolean) {
              Element rootEle = doc.createElement("logdetails");
              doc.appendChild(rootEle);
              rootEle.appendChild(infoEle);
          }
          Element root = doc.getDocumentElement();
          NodeList pNodes = root.getElementsByTagName("info");
            Node first = pNodes.item(0);
          Node node = root.insertBefore(infoEle, first);
          int length = pNodes.getLength();
            if(length > 9) {
                for(int i = (length - 1); i > 8; i = i - 1) {
                  Element element = (Element)pNodes.item(i);
                  root.removeChild(element);
              }
          }
          TransformerFactory tf = TransformerFactory.newInstance();
          Transformer transformer = tf.newTransformer();
          
          DOMSource source = new DOMSource(doc);
          transformer.setOutputProperty("encoding", "utf-8");
          transformer.setOutputProperty("indent", "yes");
          FileOutputStream outputStream = new FileOutputStream(file);
          PrintWriter pw = new PrintWriter(outputStream);
          StreamResult result = new StreamResult(pw);
          
          transformer.transform(source, result);
          
      } catch(Exception localException2) {
    	  //localException2.printStackTrace();
      }
    }
  } catch(Exception localException3) {
	  //localException3.printStackTrace();
  }  
 }

  public void WriteLogFile(int paramInt1, int paramInt2)
  {
    File localFile1 = new File("data/data/com.dewav.intexzone2");
    File localFile2 = new File("data/data/com.dewav.intexzone2/charging_log_details.xml");
    try
    {
      if (!localFile1.exists())
        localFile1.mkdirs();
      if (!localFile2.exists())
      {
        localFile2.createNewFile();
        InsertLog(true, paramInt1, paramInt2);
        return;
      }
      InsertLog(false, paramInt1, paramInt2);
      return;
    }
    catch (Exception localException4){
    	localException4.printStackTrace();
    }
  }

}