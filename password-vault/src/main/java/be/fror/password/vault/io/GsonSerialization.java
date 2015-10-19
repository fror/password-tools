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

import static java.nio.charset.StandardCharsets.UTF_8;

import be.fror.common.io.ByteSource;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

/**
 *
 * @author Olivier Grégoire
 */
class GsonSerialization implements Serialization {

  final Gson gson;

  GsonSerialization() {
    gson = new Gson();
  }

  @Override
  public <T> T read(ByteSource source, Class<T> type) throws RuntimeException {
    try (Reader reader = source.asCharSource(UTF_8).openStream()) {
      return gson.fromJson(reader, type);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
