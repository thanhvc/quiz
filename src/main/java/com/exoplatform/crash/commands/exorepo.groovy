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
package crash.commands

import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryEntry;

/**
 * @author <a href="mailto:thanhvc@exoplatform.com">Thanh Vu Cong</a>
 * @version $Revision$
 */
@Usage("Repository management")
class exorepo {
  
  @Usage("Sets current repository")
  @Command
  public Object name(@Argument String repoName) throws ScriptException {
    RepositoryService service = getRepositoryService();
    if (service != null) {
      service.setCurrentRepositoryName(repoName);
    } else {
      return "Repository service is NULL";
    }
    return "Done";
  }
  
  @Usage("Gets current repository name")
  @Command
  public Object current() throws ScriptException {
    def print = "";
    RepositoryService service = getRepositoryService();
    if (service != null) {
      print += "current repo: " + service.getCurrentRepository().getConfiguration().getName();
    } else {
      return "Repository service is NULL";
    }
    
    return print;
  }
  
  @Usage("Display all repository name")
  @Command
  public Object all() throws ScriptException {
    def print = "";
    RepositoryService service = getRepositoryService();
    if (service != null) {
      List<RepositoryEntry> entries = service.getConfig().getRepositoryConfigurations();
      entries.each {
        RepositoryEntry entry = ((RepositoryEntry)it);
        print += "${entry.name}\n";
      }
    } else {
      return "Repository service is NULL";
    }
    
    return print;
  }

  
  private RepositoryService getRepositoryService() {
    return  (RepositoryService) PortalContainer.getInstance().getComponentInstanceOfType(RepositoryService.class);
  }

}
