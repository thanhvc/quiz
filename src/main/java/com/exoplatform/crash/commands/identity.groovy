package crash.commands

import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.CRaSHCommand
import org.exoplatform.container.PortalContainer
import org.gatein.common.io.IOTools
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
import org.exoplatform.social.core.storage.impl.ActivityStorageImpl;

/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by The eXo Platform SAS
 * Author : Thanh Vu Cong
 *          thanh_vucong@exoplatform.com
 * Apr 13, 2013  
 */
@Usage("Social Identity Utilities")
public class identity extends CRaSHCommand {

  @Usage("find social identity")
  @Command
  public Object user(@Usage("given remoteId what needs to find") @Argument String remoteId) throws ScriptException {
  
    IdentityManager manager =(IdentityManager) getContainer().getComponentInstanceOfType(IdentityManager.class);
    Identity ownerIdentity = manager.getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteId, false);
    String print = "";

    print += "${ownerIdentity.remoteId} : ${ownerIdentity.profile.fullName} \n";
    
    return print;
  }
  
  @Usage("pre-computing ON for AS")
  @Command
  public Object computingOn() throws ScriptException {
  
    ActivityStorageImpl impl =(ActivityStorageImpl) getContainer().getComponentInstanceOfType(ActivityStorageImpl.class);
    impl.setInjectStreams(true);
    return "DONE => pre-computing activity stream ON";
  }
  
  @Usage("pre-computing OFF for AS")
  @Command
  public Object computingOff() throws ScriptException {
  
    ActivityStorageImpl impl =(ActivityStorageImpl) getContainer().getComponentInstanceOfType(ActivityStorageImpl.class);
    impl.setInjectStreams(false);
    return "DONE => pre-computing activity stream ON";
  }
  
  private PortalContainer getContainer() {
    return PortalContainer.getInstance();
  }
}
