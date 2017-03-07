package com.hex.util;

import com.hex.vo.TemplateVO;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.io.*;
import java.util.ArrayList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class CodeGeneratorUtil {

    public CodeGeneratorUtil() {
    }

   
    public void replaceAndWriteContentsToFile(TemplateVO poTemplateVO) {

    System.out.println("Enter into CodeGenerationUtil ...... replaceAndWriteContentsToFile method ............. ");
        
        try {

            DataInputStream dis = null;
            StringBuffer result = new StringBuffer();
            dis = new DataInputStream(new FileInputStream(poTemplateVO.getTemplateDirectory() + poTemplateVO.getTemplateFileName()));

            while (dis.available() > 0) {
                String s = dis.readLine();
                result.append(s + "\n");
            }

            if (poTemplateVO.getDynamicContent() != null) {
                Set woSet = poTemplateVO.getDynamicContent().entrySet();
                for (Iterator iterator = woSet.iterator(); iterator.hasNext();) {
                    Entry entry = (Entry) iterator.next();
                    replaceAll(result, (String) entry.getKey(), (String) entry.getValue());
                }
            }

            dis.close();

            //Generation of faces-config.xml

            String outputDirectory = poTemplateVO.getOutputDirectory();
            System.out.println(" Ouit Directory in Code Generator **** " +
                    outputDirectory);
            File file = new File(outputDirectory);
            if (file.isDirectory() == false) {
                System.out.println(
                        " The outputdirectory does not exist, making the directories...");
                file.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(outputDirectory +
                    poTemplateVO.getOutputFileName());
            fos.write(result.toString().getBytes());
            fos.close();
        } catch (Exception ex) {
            System.out.println("exception" + ex);
            ex.printStackTrace();
        }
    }

    private void replaceAll(StringBuffer buffer, String oldValue, String newValue) {
        int index = buffer.indexOf(oldValue);
        while (index != -1) {
            int len = index + oldValue.length();
            buffer = buffer.replace(index, len, newValue);
            //System.out.println("String buffer Replace All ^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + buffer);
            index = buffer.indexOf(oldValue);
        }
    }
}
