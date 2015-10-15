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
package be.fror.password.vault.core;

import be.fror.password.vault.model.Vault;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

/**
 *
 * @author Olivier Grégoire
 */
public interface VaultFormat {

  public static Vault read(ByteSource source) throws IOException, UnsupportedFormatException {
    VaultFormat[] formats = {
      Vault1Format.INSTANCE
    };
    try (PushbackInputStream in = new PushbackInputStream(source.openStream())) {
      for (VaultFormat format : formats) {
        if (format.accept(in)) {
          return format.read(in);
        }
      }
    }
    throw new UnsupportedFormatException(source.toString());
  }

  public static void write(ByteSink sink, Vault vault) throws IOException {
    try (OutputStream out = sink.openStream()) {
      Vault1Format.INSTANCE.write(out, vault);
    }
  }

  public boolean accept(PushbackInputStream in) throws IOException;

  public Vault read(InputStream in) throws IOException;

  public void write(OutputStream out, Vault vault) throws IOException;

}
