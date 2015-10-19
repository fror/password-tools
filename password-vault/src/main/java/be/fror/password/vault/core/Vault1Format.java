/*
 * Copyright 2015 Olivier Gregoire.
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

import static be.fror.common.io.ByteStreams.readFully;
import static be.fror.common.primitives.Ints.fromLittleEndianByteArray;
import static java.nio.charset.StandardCharsets.US_ASCII;

import be.fror.password.vault.model.Vault;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;

/**
 *
 * @author Olivier Gregoire
 */
enum Vault1Format implements VaultFormat {

  INSTANCE;

  private static final String TAG = "VLT1";
  private static final byte[] TAG_BYTES = TAG.getBytes(US_ASCII);

  @Override
  public boolean accept(PushbackInputStream in) throws IOException {
    byte[] tag = readFully(in, new byte[4]);
    in.unread(tag);
    return Arrays.equals(TAG_BYTES, tag);
  }

  @Override
  public Vault read(InputStream in) throws IOException {
    byte[] tag = readFully(in, new byte[4]);
    byte[] salt = readFully(in, new byte[32]);
    int iter = fromLittleEndianByteArray(readFully(in, new byte[4]));

    return null;
  }

  @Override
  public void write(OutputStream out, Vault vault) throws IOException {
    out.write(TAG_BYTES);
    out.flush();
  }

}
