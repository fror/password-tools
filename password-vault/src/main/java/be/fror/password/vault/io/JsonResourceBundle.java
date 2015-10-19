/*
 * Copyright 2015 Olivier Grégoire.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.fror.password.vault.io;

import static be.fror.common.base.Preconditions.checkNotNull;

import be.fror.common.io.ByteSource;
import be.fror.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Olivier Grégoire
 */
public class JsonResourceBundle {

  private static class JsonControl extends ResourceBundle.Control {

    private static final String FORMAT = "json";

    @Override
    public List<String> getFormats(String baseName) {
      checkNotNull(baseName);
      return Arrays.asList(FORMAT);
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
      if (!FORMAT.equals(format)) {
        return null;
      }
      String bundleName = toBundleName(baseName, locale);
      String resourceName = toResourceName(bundleName, format);
      ByteSource source = null;
      if (reload) {
        URL url = loader.getResource(format);
        if (url != null) {
          URLConnection connection = url.openConnection();
          if (connection != null) {
            connection.setUseCaches(false);
            source = new ByteSource() {
              @Override
              protected InputStream doOpenStream() throws IOException {
                return connection.getInputStream();
              }
            };
          }
        }
      } else {
        source = Resources.asByteSource(Resources.getResource(resourceName));
      }
      return null; // TODO implement
    }

  }
}
