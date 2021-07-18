/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.mtp;


class MtpPropertyGroup {
    private static final java.lang.String TAG = "MtpPropertyGroup";

    private class Property {
        // MTP property code
        int code;

        // MTP data type
        int type;

        // column index for our query
        int column;

        Property(int code, int type, int column) {
            this.code = code;
            this.type = type;
            this.column = column;
        }
    }

    private final android.mtp.MtpDatabase mDatabase;

    private final android.content.ContentProviderClient mProvider;

    private final java.lang.String mVolumeName;

    private final android.net.Uri mUri;

    // list of all properties in this group
    private final android.mtp.MtpPropertyGroup.Property[] mProperties;

    // list of columns for database query
    private java.lang.String[] mColumns;

    private static final java.lang.String ID_WHERE = android.provider.MediaStore.Files.FileColumns._ID + "=?";

    private static final java.lang.String FORMAT_WHERE = android.provider.MediaStore.Files.FileColumns.FORMAT + "=?";

    private static final java.lang.String ID_FORMAT_WHERE = (android.mtp.MtpPropertyGroup.ID_WHERE + " AND ") + android.mtp.MtpPropertyGroup.FORMAT_WHERE;

    private static final java.lang.String PARENT_WHERE = android.provider.MediaStore.Files.FileColumns.PARENT + "=?";

    private static final java.lang.String PARENT_FORMAT_WHERE = (android.mtp.MtpPropertyGroup.PARENT_WHERE + " AND ") + android.mtp.MtpPropertyGroup.FORMAT_WHERE;

    // constructs a property group for a list of properties
    public MtpPropertyGroup(android.mtp.MtpDatabase database, android.content.ContentProviderClient provider, java.lang.String volumeName, int[] properties) {
        mDatabase = database;
        mProvider = provider;
        mVolumeName = volumeName;
        mUri = android.provider.MediaStore.Files.getMtpObjectsUri(volumeName);
        int count = properties.length;
        java.util.ArrayList<java.lang.String> columns = new java.util.ArrayList<java.lang.String>(count);
        columns.add(android.provider.MediaStore.Files.FileColumns._ID);
        mProperties = new android.mtp.MtpPropertyGroup.Property[count];
        for (int i = 0; i < count; i++) {
            mProperties[i] = createProperty(properties[i], columns);
        }
        count = columns.size();
        mColumns = new java.lang.String[count];
        for (int i = 0; i < count; i++) {
            mColumns[i] = columns.get(i);
        }
    }

    private android.mtp.MtpPropertyGroup.Property createProperty(int code, java.util.ArrayList<java.lang.String> columns) {
        java.lang.String column = null;
        int type;
        switch (code) {
            case android.mtp.MtpConstants.PROPERTY_STORAGE_ID :
                column = android.provider.MediaStore.Files.FileColumns.STORAGE_ID;
                type = android.mtp.MtpConstants.TYPE_UINT32;
                break;
            case android.mtp.MtpConstants.PROPERTY_OBJECT_FORMAT :
                column = android.provider.MediaStore.Files.FileColumns.FORMAT;
                type = android.mtp.MtpConstants.TYPE_UINT16;
                break;
            case android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS :
                // protection status is always 0
                type = android.mtp.MtpConstants.TYPE_UINT16;
                break;
            case android.mtp.MtpConstants.PROPERTY_OBJECT_SIZE :
                column = android.provider.MediaStore.Files.FileColumns.SIZE;
                type = android.mtp.MtpConstants.TYPE_UINT64;
                break;
            case android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME :
                column = android.provider.MediaStore.Files.FileColumns.DATA;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_NAME :
                column = android.provider.MediaStore.MediaColumns.TITLE;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED :
                column = android.provider.MediaStore.Files.FileColumns.DATE_MODIFIED;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_DATE_ADDED :
                column = android.provider.MediaStore.Files.FileColumns.DATE_ADDED;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE :
                column = android.provider.MediaStore.Audio.AudioColumns.YEAR;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_PARENT_OBJECT :
                column = android.provider.MediaStore.Files.FileColumns.PARENT;
                type = android.mtp.MtpConstants.TYPE_UINT32;
                break;
            case android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID :
                // PUID is concatenation of storageID and object handle
                column = android.provider.MediaStore.Files.FileColumns.STORAGE_ID;
                type = android.mtp.MtpConstants.TYPE_UINT128;
                break;
            case android.mtp.MtpConstants.PROPERTY_DURATION :
                column = android.provider.MediaStore.Audio.AudioColumns.DURATION;
                type = android.mtp.MtpConstants.TYPE_UINT32;
                break;
            case android.mtp.MtpConstants.PROPERTY_TRACK :
                column = android.provider.MediaStore.Audio.AudioColumns.TRACK;
                type = android.mtp.MtpConstants.TYPE_UINT16;
                break;
            case android.mtp.MtpConstants.PROPERTY_DISPLAY_NAME :
                column = android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_ARTIST :
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_ALBUM_NAME :
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_ALBUM_ARTIST :
                column = android.provider.MediaStore.Audio.AudioColumns.ALBUM_ARTIST;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_GENRE :
                // genre requires a special query
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_COMPOSER :
                column = android.provider.MediaStore.Audio.AudioColumns.COMPOSER;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_DESCRIPTION :
                column = android.provider.MediaStore.Images.ImageColumns.DESCRIPTION;
                type = android.mtp.MtpConstants.TYPE_STR;
                break;
            case android.mtp.MtpConstants.PROPERTY_AUDIO_WAVE_CODEC :
            case android.mtp.MtpConstants.PROPERTY_AUDIO_BITRATE :
            case android.mtp.MtpConstants.PROPERTY_SAMPLE_RATE :
                // these are special cased
                type = android.mtp.MtpConstants.TYPE_UINT32;
                break;
            case android.mtp.MtpConstants.PROPERTY_BITRATE_TYPE :
            case android.mtp.MtpConstants.PROPERTY_NUMBER_OF_CHANNELS :
                // these are special cased
                type = android.mtp.MtpConstants.TYPE_UINT16;
                break;
            default :
                type = android.mtp.MtpConstants.TYPE_UNDEFINED;
                android.util.Log.e(android.mtp.MtpPropertyGroup.TAG, "unsupported property " + code);
                break;
        }
        if (column != null) {
            columns.add(column);
            return new android.mtp.MtpPropertyGroup.Property(code, type, columns.size() - 1);
        } else {
            return new android.mtp.MtpPropertyGroup.Property(code, type, -1);
        }
    }

    private java.lang.String queryString(int id, java.lang.String column) {
        android.database.Cursor c = null;
        try {
            // for now we are only reading properties from the "objects" table
            c = mProvider.query(mUri, new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID, column }, android.mtp.MtpPropertyGroup.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(id) }, null, null);
            if ((c != null) && c.moveToNext()) {
                return c.getString(1);
            } else {
                return "";
            }
        } catch (java.lang.Exception e) {
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private java.lang.String queryAudio(int id, java.lang.String column) {
        android.database.Cursor c = null;
        try {
            c = mProvider.query(android.provider.MediaStore.Audio.Media.getContentUri(mVolumeName), new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID, column }, android.mtp.MtpPropertyGroup.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(id) }, null, null);
            if ((c != null) && c.moveToNext()) {
                return c.getString(1);
            } else {
                return "";
            }
        } catch (java.lang.Exception e) {
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private java.lang.String queryGenre(int id) {
        android.database.Cursor c = null;
        try {
            android.net.Uri uri = android.provider.MediaStore.Audio.Genres.getContentUriForAudioId(mVolumeName, id);
            c = mProvider.query(uri, new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID, android.provider.MediaStore.Audio.GenresColumns.NAME }, null, null, null, null);
            if ((c != null) && c.moveToNext()) {
                return c.getString(1);
            } else {
                return "";
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(android.mtp.MtpPropertyGroup.TAG, "queryGenre exception", e);
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private java.lang.Long queryLong(int id, java.lang.String column) {
        android.database.Cursor c = null;
        try {
            // for now we are only reading properties from the "objects" table
            c = mProvider.query(mUri, new java.lang.String[]{ android.provider.MediaStore.Files.FileColumns._ID, column }, android.mtp.MtpPropertyGroup.ID_WHERE, new java.lang.String[]{ java.lang.Integer.toString(id) }, null, null);
            if ((c != null) && c.moveToNext()) {
                return new java.lang.Long(c.getLong(1));
            }
        } catch (java.lang.Exception e) {
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    private static java.lang.String nameFromPath(java.lang.String path) {
        // extract name from full path
        int start = 0;
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash >= 0) {
            start = lastSlash + 1;
        }
        int end = path.length();
        if ((end - start) > 255) {
            end = start + 255;
        }
        return path.substring(start, end);
    }

    android.mtp.MtpPropertyList getPropertyList(int handle, int format, int depth) {
        // Log.d(TAG, "getPropertyList handle: " + handle + " format: " + format + " depth: " + depth);
        if (depth > 1) {
            // we only support depth 0 and 1
            // depth 0: single object, depth 1: immediate children
            return new android.mtp.MtpPropertyList(0, android.mtp.MtpConstants.RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED);
        }
        java.lang.String where;
        java.lang.String[] whereArgs;
        if (format == 0) {
            if (handle == 0xffffffff) {
                // select all objects
                where = null;
                whereArgs = null;
            } else {
                whereArgs = new java.lang.String[]{ java.lang.Integer.toString(handle) };
                if (depth == 1) {
                    where = android.mtp.MtpPropertyGroup.PARENT_WHERE;
                } else {
                    where = android.mtp.MtpPropertyGroup.ID_WHERE;
                }
            }
        } else {
            if (handle == 0xffffffff) {
                // select all objects with given format
                where = android.mtp.MtpPropertyGroup.FORMAT_WHERE;
                whereArgs = new java.lang.String[]{ java.lang.Integer.toString(format) };
            } else {
                whereArgs = new java.lang.String[]{ java.lang.Integer.toString(handle), java.lang.Integer.toString(format) };
                if (depth == 1) {
                    where = android.mtp.MtpPropertyGroup.PARENT_FORMAT_WHERE;
                } else {
                    where = android.mtp.MtpPropertyGroup.ID_FORMAT_WHERE;
                }
            }
        }
        android.database.Cursor c = null;
        try {
            // don't query if not necessary
            if (((depth > 0) || (handle == 0xffffffff)) || (mColumns.length > 1)) {
                c = mProvider.query(mUri, mColumns, where, whereArgs, null, null);
                if (c == null) {
                    return new android.mtp.MtpPropertyList(0, android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
                }
            }
            int count = (c == null) ? 1 : c.getCount();
            android.mtp.MtpPropertyList result = new android.mtp.MtpPropertyList(count * mProperties.length, android.mtp.MtpConstants.RESPONSE_OK);
            // iterate over all objects in the query
            for (int objectIndex = 0; objectIndex < count; objectIndex++) {
                if (c != null) {
                    c.moveToNext();
                    handle = ((int) (c.getLong(0)));
                }
                // iterate over all properties in the query for the given object
                for (int propertyIndex = 0; propertyIndex < mProperties.length; propertyIndex++) {
                    android.mtp.MtpPropertyGroup.Property property = mProperties[propertyIndex];
                    int propertyCode = property.code;
                    int column = property.column;
                    // handle some special cases
                    switch (propertyCode) {
                        case android.mtp.MtpConstants.PROPERTY_PROTECTION_STATUS :
                            // protection status is always 0
                            result.append(handle, propertyCode, android.mtp.MtpConstants.TYPE_UINT16, 0);
                            break;
                        case android.mtp.MtpConstants.PROPERTY_OBJECT_FILE_NAME :
                            // special case - need to extract file name from full path
                            java.lang.String value = c.getString(column);
                            if (value != null) {
                                result.append(handle, propertyCode, android.mtp.MtpPropertyGroup.nameFromPath(value));
                            } else {
                                result.setResult(android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
                            }
                            break;
                        case android.mtp.MtpConstants.PROPERTY_NAME :
                            // first try title
                            java.lang.String name = c.getString(column);
                            // then try name
                            if (name == null) {
                                name = queryString(handle, android.provider.MediaStore.Audio.PlaylistsColumns.NAME);
                            }
                            // if title and name fail, extract name from full path
                            if (name == null) {
                                name = queryString(handle, android.provider.MediaStore.Files.FileColumns.DATA);
                                if (name != null) {
                                    name = android.mtp.MtpPropertyGroup.nameFromPath(name);
                                }
                            }
                            if (name != null) {
                                result.append(handle, propertyCode, name);
                            } else {
                                result.setResult(android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
                            }
                            break;
                        case android.mtp.MtpConstants.PROPERTY_DATE_MODIFIED :
                        case android.mtp.MtpConstants.PROPERTY_DATE_ADDED :
                            // convert from seconds to DateTime
                            result.append(handle, propertyCode, format_date_time(c.getInt(column)));
                            break;
                        case android.mtp.MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE :
                            // release date is stored internally as just the year
                            int year = c.getInt(column);
                            java.lang.String dateTime = java.lang.Integer.toString(year) + "0101T000000";
                            result.append(handle, propertyCode, dateTime);
                            break;
                        case android.mtp.MtpConstants.PROPERTY_PERSISTENT_UID :
                            // PUID is concatenation of storageID and object handle
                            long puid = c.getLong(column);
                            puid <<= 32;
                            puid += handle;
                            result.append(handle, propertyCode, android.mtp.MtpConstants.TYPE_UINT128, puid);
                            break;
                        case android.mtp.MtpConstants.PROPERTY_TRACK :
                            result.append(handle, propertyCode, android.mtp.MtpConstants.TYPE_UINT16, c.getInt(column) % 1000);
                            break;
                        case android.mtp.MtpConstants.PROPERTY_ARTIST :
                            result.append(handle, propertyCode, queryAudio(handle, android.provider.MediaStore.Audio.AudioColumns.ARTIST));
                            break;
                        case android.mtp.MtpConstants.PROPERTY_ALBUM_NAME :
                            result.append(handle, propertyCode, queryAudio(handle, android.provider.MediaStore.Audio.AudioColumns.ALBUM));
                            break;
                        case android.mtp.MtpConstants.PROPERTY_GENRE :
                            java.lang.String genre = queryGenre(handle);
                            if (genre != null) {
                                result.append(handle, propertyCode, genre);
                            } else {
                                result.setResult(android.mtp.MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE);
                            }
                            break;
                        case android.mtp.MtpConstants.PROPERTY_AUDIO_WAVE_CODEC :
                        case android.mtp.MtpConstants.PROPERTY_AUDIO_BITRATE :
                        case android.mtp.MtpConstants.PROPERTY_SAMPLE_RATE :
                            // we don't have these in our database, so return 0
                            result.append(handle, propertyCode, android.mtp.MtpConstants.TYPE_UINT32, 0);
                            break;
                        case android.mtp.MtpConstants.PROPERTY_BITRATE_TYPE :
                        case android.mtp.MtpConstants.PROPERTY_NUMBER_OF_CHANNELS :
                            // we don't have these in our database, so return 0
                            result.append(handle, propertyCode, android.mtp.MtpConstants.TYPE_UINT16, 0);
                            break;
                        default :
                            if (property.type == android.mtp.MtpConstants.TYPE_STR) {
                                result.append(handle, propertyCode, c.getString(column));
                            } else
                                if (property.type == android.mtp.MtpConstants.TYPE_UNDEFINED) {
                                    result.append(handle, propertyCode, property.type, 0);
                                } else {
                                    result.append(handle, propertyCode, property.type, c.getLong(column));
                                }

                            break;
                    }
                }
            }
            return result;
        } catch (android.os.RemoteException e) {
            return new android.mtp.MtpPropertyList(0, android.mtp.MtpConstants.RESPONSE_GENERAL_ERROR);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        // impossible to get here, so no return statement
    }

    private native java.lang.String format_date_time(long seconds);
}

