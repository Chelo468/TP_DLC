/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dlc.indedxador.drive;

/**
 *
 * @author Gabriel
 */

import com.google.api.client.util.DateTime;
import java.io.IOException;
import java.util.List;
 
 
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
 
public class ReadFromDrive {
    private static Map<String, String> list;
    // com.google.api.services.drive.model.File
    public static final Map<String, String> obtenerArchivos(String googleFolderIdParent, Drive driveService)
            throws IOException {
 
        String pageToken = null;
        if(list == null)
        {
            list = new HashMap<String, String>();
        }
  
        do {           
            FileList result = driveService.files().list().setQ("'" + googleFolderIdParent + "' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false")
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name, modifiedTime)")
                    .setPageToken(pageToken).execute();
             List<File> files = result.getFiles();
            for (int i = 0; i < files.size(); i++) {         
                String name = files.get(i).getName();
                String id = files.get(i).getId();
                list.put(name, id);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        
        return list;
    }

    
    public static void descargarArchivo(String name, String textID, Drive driveService ) throws IOException {
        OutputStream outputStream = new FileOutputStream(name);
        driveService.files().get(textID).executeMediaAndDownloadTo(outputStream);
      //  return outputStream;
    }
    
}
