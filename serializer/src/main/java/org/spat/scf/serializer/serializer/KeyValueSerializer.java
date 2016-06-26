/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.spat.scf.serializer.serializer;

import org.spat.scf.serializer.classes.GKeyValuePair;
import org.spat.scf.serializer.component.SCFInStream;
import org.spat.scf.serializer.component.SCFOutStream;
import org.spat.scf.serializer.utility.TypeHelper;

/**
 *
 * @author Administrator
 */
class KeyValueSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, SCFOutStream outStream) throws Exception {
        if (obj == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        }
        Class type = obj.getClass();
        int typeId = TypeHelper.GetTypeId(type);
        outStream.WriteInt32(typeId);
        if (outStream.WriteRef(obj)) {
            return;
        }
        GKeyValuePair obj2 = (GKeyValuePair) obj;
        Object key = obj2.getKey();
        Object value = obj2.getValue();
        Class itemKeyType = key.getClass();
        int itemKeyTypeId = TypeHelper.GetTypeId(itemKeyType);
        outStream.WriteInt32(itemKeyTypeId);
        SerializerFactory.GetSerializer(itemKeyType).WriteObject(key, outStream);

        if (value == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        } else {

            Class itemValueType = value.getClass();
            int itemValueTypeId = TypeHelper.GetTypeId(itemValueType);
            outStream.WriteInt32(itemValueTypeId);
            SerializerFactory.GetSerializer(itemValueType).WriteObject(value, outStream);
        }
    }

    @Override
    public Object ReadObject(SCFInStream inStream, Class defType) throws Exception {
        int typeId = inStream.ReadInt32();
        if (typeId == 0) {
            return null;
        }
        byte isRef = (byte) inStream.read();
        int hashcode = inStream.ReadInt32();
        if (isRef > 0) {
            return inStream.GetRef(hashcode);
        }

        int itemKeyTypeId = inStream.ReadInt32();
        Class itemKeyType = TypeHelper.GetType(itemKeyTypeId);
        if (itemKeyType == null) {
            throw new ClassNotFoundException("Cannot find class with typId,target class:KeyValue[key]" + ",typeId:" + itemKeyTypeId);
        }
        Object key = SerializerFactory.GetSerializer(itemKeyType).ReadObject(inStream, itemKeyType);

        int itemValueTypeId = inStream.ReadInt32();
        Object value = null;
        if (itemValueTypeId != 0) {
            Class itemValueType = TypeHelper.GetType(itemValueTypeId);
            if (itemValueType == null) {
                throw new ClassNotFoundException("Cannot find class with typId,target class:KeyValue[value]" + ",typeId:" + itemValueTypeId);
            }
            value = SerializerFactory.GetSerializer(itemValueType).ReadObject(inStream, itemValueType);
        }
        GKeyValuePair kv = new GKeyValuePair(key, value);
        inStream.SetRef(hashcode, kv);
        return kv;
    }
}
