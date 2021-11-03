/**
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thierrysquirrel.ants.core.factory;

import com.github.thierrysquirrel.ants.core.factory.constant.BitOperationConstant;
import com.github.thierrysquirrel.ants.core.strategy.DataLengthLevelStrategy;
import com.github.thierrysquirrel.ants.core.template.factory.ByteBufferFactory;

import java.nio.ByteBuffer;

/**
 * Classname: BitOperationFactory
 * Description:
 * Date: 2021/11/3 14:09
 *
 * @author ThierrySquirrel
 * @since JDK 11
 */
public final class BitOperationFactory {
    private BitOperationFactory() {
    }

    public static byte[] commonSerializeInteger(Object data) {
        long dataLong = Long.parseLong (data.toString ());
        if (DataLengthLevelStrategy.isReverse (dataLong)) {
            dataLong = -dataLong;
        }
        int dataBytesSize = DataLengthLevelStrategy.getDataByteSize (dataLong);

        ByteBuffer byteBuffer = ByteBuffer.allocate (dataBytesSize);
        int offset = dataBytesSize - 1;
        while (offset >= 0) {
            int rightShift = offset * BitOperationConstant.BYTE_BIT_SIZE;
            offset--;
            long rightShiftValue = dataLong >> rightShift;
            byte dateByte = (byte) (rightShiftValue & BitOperationConstant.UNSIGNED_BYTE_MAX);
            byteBuffer.put (dateByte);
        }
        byteBuffer.flip ();
        return ByteBufferFactory.getAllBytes (byteBuffer);
    }

    public static long commonDeSerializeInteger(byte[] data){
        long dataLong = 0;
        int dataLength = data.length;
        int offset = dataLength - 1;
        for (byte dataByte : data) {
            long dataByteLong = dataByte;
            if (dataByteLong < 0) {
                dataByteLong += BitOperationConstant.UNSIGNED_BYTE_TO_SINGED;
            }
            int leftShift = offset * BitOperationConstant.BYTE_BIT_SIZE;
            offset--;
            dataLong += dataByteLong << leftShift;
        }
        return dataLong;
    }
}
