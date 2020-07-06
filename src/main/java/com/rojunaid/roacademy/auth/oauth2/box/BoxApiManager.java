package com.rojunaid.roacademy.auth.oauth2.box;

import com.box.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoxApiManager {

  @Autowired private BoxApiConnectionProvider apiConnectionProvider;

  public BoxFolder.Info createFolder(String name, String parentId) throws BoxAPIException {

    BoxFolder parentFolder = new BoxFolder(apiConnectionProvider.getConnection(), parentId);
    BoxFolder.Info childFolderInfo = parentFolder.createFolder(name);
    return childFolderInfo;
  }

  public BoxFolder.Info getFolderByName(String name, String parentId) throws BoxAPIException {

    BoxFolder folder = new BoxFolder(apiConnectionProvider.getConnection(), parentId);
    for (BoxItem.Info itemInfo : folder) {
      if (itemInfo instanceof BoxFolder.Info) {
        if (itemInfo.getName().equals(name)) {
          return (BoxFolder.Info) itemInfo;
        }
      }
    }
    return null;
  }

  public String getSharedLink(String fileId) {
    BoxFile file = new BoxFile(apiConnectionProvider.getConnection(), fileId);
    BoxSharedLink.Permissions permissions = new BoxSharedLink.Permissions();
    permissions.setCanDownload(true);
    permissions.setCanPreview(true);
    BoxSharedLink sharedLink = file.createSharedLink(BoxSharedLink.Access.OPEN, null, permissions);
    return sharedLink.getURL();
  }
}
