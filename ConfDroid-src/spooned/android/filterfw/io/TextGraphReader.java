/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.io;


/**
 *
 *
 * @unknown 
 */
public class TextGraphReader extends android.filterfw.io.GraphReader {
    private java.util.ArrayList<android.filterfw.io.TextGraphReader.Command> mCommands = new java.util.ArrayList<android.filterfw.io.TextGraphReader.Command>();

    private android.filterfw.core.Filter mCurrentFilter;

    private android.filterfw.core.FilterGraph mCurrentGraph;

    private android.filterfw.core.KeyValueMap mBoundReferences;

    private android.filterfw.core.KeyValueMap mSettings;

    private android.filterfw.core.FilterFactory mFactory;

    private interface Command {
        public void execute(android.filterfw.io.TextGraphReader reader) throws android.filterfw.io.GraphIOException;
    }

    private class ImportPackageCommand implements android.filterfw.io.TextGraphReader.Command {
        private java.lang.String mPackageName;

        public ImportPackageCommand(java.lang.String packageName) {
            mPackageName = packageName;
        }

        @java.lang.Override
        public void execute(android.filterfw.io.TextGraphReader reader) throws android.filterfw.io.GraphIOException {
            try {
                reader.mFactory.addPackage(mPackageName);
            } catch (java.lang.IllegalArgumentException e) {
                throw new android.filterfw.io.GraphIOException(e.getMessage());
            }
        }
    }

    private class AddLibraryCommand implements android.filterfw.io.TextGraphReader.Command {
        private java.lang.String mLibraryName;

        public AddLibraryCommand(java.lang.String libraryName) {
            mLibraryName = libraryName;
        }

        @java.lang.Override
        public void execute(android.filterfw.io.TextGraphReader reader) {
            reader.mFactory.addFilterLibrary(mLibraryName);
        }
    }

    private class AllocateFilterCommand implements android.filterfw.io.TextGraphReader.Command {
        private java.lang.String mClassName;

        private java.lang.String mFilterName;

        public AllocateFilterCommand(java.lang.String className, java.lang.String filterName) {
            mClassName = className;
            mFilterName = filterName;
        }

        public void execute(android.filterfw.io.TextGraphReader reader) throws android.filterfw.io.GraphIOException {
            // Create the filter
            android.filterfw.core.Filter filter = null;
            try {
                filter = reader.mFactory.createFilterByClassName(mClassName, mFilterName);
            } catch (java.lang.IllegalArgumentException e) {
                throw new android.filterfw.io.GraphIOException(e.getMessage());
            }
            // Set it as the current filter
            reader.mCurrentFilter = filter;
        }
    }

    private class InitFilterCommand implements android.filterfw.io.TextGraphReader.Command {
        private android.filterfw.core.KeyValueMap mParams;

        public InitFilterCommand(android.filterfw.core.KeyValueMap params) {
            mParams = params;
        }

        @java.lang.Override
        public void execute(android.filterfw.io.TextGraphReader reader) throws android.filterfw.io.GraphIOException {
            android.filterfw.core.Filter filter = reader.mCurrentFilter;
            try {
                filter.initWithValueMap(mParams);
            } catch (android.filterfw.core.ProtocolException e) {
                throw new android.filterfw.io.GraphIOException(e.getMessage());
            }
            reader.mCurrentGraph.addFilter(mCurrentFilter);
        }
    }

    private class ConnectCommand implements android.filterfw.io.TextGraphReader.Command {
        private java.lang.String mSourceFilter;

        private java.lang.String mSourcePort;

        private java.lang.String mTargetFilter;

        private java.lang.String mTargetName;

        public ConnectCommand(java.lang.String sourceFilter, java.lang.String sourcePort, java.lang.String targetFilter, java.lang.String targetName) {
            mSourceFilter = sourceFilter;
            mSourcePort = sourcePort;
            mTargetFilter = targetFilter;
            mTargetName = targetName;
        }

        @java.lang.Override
        public void execute(android.filterfw.io.TextGraphReader reader) {
            reader.mCurrentGraph.connect(mSourceFilter, mSourcePort, mTargetFilter, mTargetName);
        }
    }

    @java.lang.Override
    public android.filterfw.core.FilterGraph readGraphString(java.lang.String graphString) throws android.filterfw.io.GraphIOException {
        android.filterfw.core.FilterGraph result = new android.filterfw.core.FilterGraph();
        reset();
        mCurrentGraph = result;
        parseString(graphString);
        applySettings();
        executeCommands();
        reset();
        return result;
    }

    private void reset() {
        mCurrentGraph = null;
        mCurrentFilter = null;
        mCommands.clear();
        mBoundReferences = new android.filterfw.core.KeyValueMap();
        mSettings = new android.filterfw.core.KeyValueMap();
        mFactory = new android.filterfw.core.FilterFactory();
    }

    private void parseString(java.lang.String graphString) throws android.filterfw.io.GraphIOException {
        final java.util.regex.Pattern commandPattern = java.util.regex.Pattern.compile("@[a-zA-Z]+");
        final java.util.regex.Pattern curlyClosePattern = java.util.regex.Pattern.compile("\\}");
        final java.util.regex.Pattern curlyOpenPattern = java.util.regex.Pattern.compile("\\{");
        final java.util.regex.Pattern ignorePattern = java.util.regex.Pattern.compile("(\\s+|//[^\\n]*\\n)+");
        final java.util.regex.Pattern packageNamePattern = java.util.regex.Pattern.compile("[a-zA-Z\\.]+");
        final java.util.regex.Pattern libraryNamePattern = java.util.regex.Pattern.compile("[a-zA-Z\\./:]+");
        final java.util.regex.Pattern portPattern = java.util.regex.Pattern.compile("\\[[a-zA-Z0-9\\-_]+\\]");
        final java.util.regex.Pattern rightArrowPattern = java.util.regex.Pattern.compile("=>");
        final java.util.regex.Pattern semicolonPattern = java.util.regex.Pattern.compile(";");
        final java.util.regex.Pattern wordPattern = java.util.regex.Pattern.compile("[a-zA-Z0-9\\-_]+");
        final int STATE_COMMAND = 0;
        final int STATE_IMPORT_PKG = 1;
        final int STATE_ADD_LIBRARY = 2;
        final int STATE_FILTER_CLASS = 3;
        final int STATE_FILTER_NAME = 4;
        final int STATE_CURLY_OPEN = 5;
        final int STATE_PARAMETERS = 6;
        final int STATE_CURLY_CLOSE = 7;
        final int STATE_SOURCE_FILTERNAME = 8;
        final int STATE_SOURCE_PORT = 9;
        final int STATE_RIGHT_ARROW = 10;
        final int STATE_TARGET_FILTERNAME = 11;
        final int STATE_TARGET_PORT = 12;
        final int STATE_ASSIGNMENT = 13;
        final int STATE_EXTERNAL = 14;
        final int STATE_SETTING = 15;
        final int STATE_SEMICOLON = 16;
        int state = STATE_COMMAND;
        android.filterfw.io.PatternScanner scanner = new android.filterfw.io.PatternScanner(graphString, ignorePattern);
        java.lang.String curClassName = null;
        java.lang.String curSourceFilterName = null;
        java.lang.String curSourcePortName = null;
        java.lang.String curTargetFilterName = null;
        java.lang.String curTargetPortName = null;
        // State machine main loop
        while (!scanner.atEnd()) {
            switch (state) {
                case STATE_COMMAND :
                    {
                        java.lang.String curCommand = scanner.eat(commandPattern, "<command>");
                        if (curCommand.equals("@import")) {
                            state = STATE_IMPORT_PKG;
                        } else
                            if (curCommand.equals("@library")) {
                                state = STATE_ADD_LIBRARY;
                            } else
                                if (curCommand.equals("@filter")) {
                                    state = STATE_FILTER_CLASS;
                                } else
                                    if (curCommand.equals("@connect")) {
                                        state = STATE_SOURCE_FILTERNAME;
                                    } else
                                        if (curCommand.equals("@set")) {
                                            state = STATE_ASSIGNMENT;
                                        } else
                                            if (curCommand.equals("@external")) {
                                                state = STATE_EXTERNAL;
                                            } else
                                                if (curCommand.equals("@setting")) {
                                                    state = STATE_SETTING;
                                                } else {
                                                    throw new android.filterfw.io.GraphIOException(("Unknown command '" + curCommand) + "'!");
                                                }






                        break;
                    }
                case STATE_IMPORT_PKG :
                    {
                        java.lang.String packageName = scanner.eat(packageNamePattern, "<package-name>");
                        mCommands.add(new android.filterfw.io.TextGraphReader.ImportPackageCommand(packageName));
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_ADD_LIBRARY :
                    {
                        java.lang.String libraryName = scanner.eat(libraryNamePattern, "<library-name>");
                        mCommands.add(new android.filterfw.io.TextGraphReader.AddLibraryCommand(libraryName));
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_FILTER_CLASS :
                    curClassName = scanner.eat(wordPattern, "<class-name>");
                    state = STATE_FILTER_NAME;
                    break;
                case STATE_FILTER_NAME :
                    {
                        java.lang.String curFilterName = scanner.eat(wordPattern, "<filter-name>");
                        mCommands.add(new android.filterfw.io.TextGraphReader.AllocateFilterCommand(curClassName, curFilterName));
                        state = STATE_CURLY_OPEN;
                        break;
                    }
                case STATE_CURLY_OPEN :
                    scanner.eat(curlyOpenPattern, "{");
                    state = STATE_PARAMETERS;
                    break;
                case STATE_PARAMETERS :
                    {
                        android.filterfw.core.KeyValueMap params = readKeyValueAssignments(scanner, curlyClosePattern);
                        mCommands.add(new android.filterfw.io.TextGraphReader.InitFilterCommand(params));
                        state = STATE_CURLY_CLOSE;
                        break;
                    }
                case STATE_CURLY_CLOSE :
                    scanner.eat(curlyClosePattern, "}");
                    state = STATE_COMMAND;
                    break;
                case STATE_SOURCE_FILTERNAME :
                    curSourceFilterName = scanner.eat(wordPattern, "<source-filter-name>");
                    state = STATE_SOURCE_PORT;
                    break;
                case STATE_SOURCE_PORT :
                    {
                        java.lang.String portString = scanner.eat(portPattern, "[<source-port-name>]");
                        curSourcePortName = portString.substring(1, portString.length() - 1);
                        state = STATE_RIGHT_ARROW;
                        break;
                    }
                case STATE_RIGHT_ARROW :
                    scanner.eat(rightArrowPattern, "=>");
                    state = STATE_TARGET_FILTERNAME;
                    break;
                case STATE_TARGET_FILTERNAME :
                    curTargetFilterName = scanner.eat(wordPattern, "<target-filter-name>");
                    state = STATE_TARGET_PORT;
                    break;
                case STATE_TARGET_PORT :
                    {
                        java.lang.String portString = scanner.eat(portPattern, "[<target-port-name>]");
                        curTargetPortName = portString.substring(1, portString.length() - 1);
                        mCommands.add(new android.filterfw.io.TextGraphReader.ConnectCommand(curSourceFilterName, curSourcePortName, curTargetFilterName, curTargetPortName));
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_ASSIGNMENT :
                    {
                        android.filterfw.core.KeyValueMap assignment = readKeyValueAssignments(scanner, semicolonPattern);
                        mBoundReferences.putAll(assignment);
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_EXTERNAL :
                    {
                        java.lang.String externalName = scanner.eat(wordPattern, "<external-identifier>");
                        bindExternal(externalName);
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_SETTING :
                    {
                        android.filterfw.core.KeyValueMap setting = readKeyValueAssignments(scanner, semicolonPattern);
                        mSettings.putAll(setting);
                        state = STATE_SEMICOLON;
                        break;
                    }
                case STATE_SEMICOLON :
                    scanner.eat(semicolonPattern, ";");
                    state = STATE_COMMAND;
                    break;
            }
        } 
        // Make sure end of input was expected
        if ((state != STATE_SEMICOLON) && (state != STATE_COMMAND)) {
            throw new android.filterfw.io.GraphIOException("Unexpected end of input!");
        }
    }

    @java.lang.Override
    public android.filterfw.core.KeyValueMap readKeyValueAssignments(java.lang.String assignments) throws android.filterfw.io.GraphIOException {
        final java.util.regex.Pattern ignorePattern = java.util.regex.Pattern.compile("\\s+");
        android.filterfw.io.PatternScanner scanner = new android.filterfw.io.PatternScanner(assignments, ignorePattern);
        return readKeyValueAssignments(scanner, null);
    }

    private android.filterfw.core.KeyValueMap readKeyValueAssignments(android.filterfw.io.PatternScanner scanner, java.util.regex.Pattern endPattern) throws android.filterfw.io.GraphIOException {
        // Our parser is a state-machine, and these are our states
        final int STATE_IDENTIFIER = 0;
        final int STATE_EQUALS = 1;
        final int STATE_VALUE = 2;
        final int STATE_POST_VALUE = 3;
        final java.util.regex.Pattern equalsPattern = java.util.regex.Pattern.compile("=");
        final java.util.regex.Pattern semicolonPattern = java.util.regex.Pattern.compile(";");
        final java.util.regex.Pattern wordPattern = java.util.regex.Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]*");
        final java.util.regex.Pattern stringPattern = java.util.regex.Pattern.compile("\'[^\']*\'|\\\"[^\\\"]*\\\"");
        final java.util.regex.Pattern intPattern = java.util.regex.Pattern.compile("[0-9]+");
        final java.util.regex.Pattern floatPattern = java.util.regex.Pattern.compile("[0-9]*\\.[0-9]+f?");
        final java.util.regex.Pattern referencePattern = java.util.regex.Pattern.compile("\\$[a-zA-Z]+[a-zA-Z0-9]");
        final java.util.regex.Pattern booleanPattern = java.util.regex.Pattern.compile("true|false");
        int state = STATE_IDENTIFIER;
        android.filterfw.core.KeyValueMap newVals = new android.filterfw.core.KeyValueMap();
        java.lang.String curKey = null;
        java.lang.String curValue = null;
        while ((!scanner.atEnd()) && (!((endPattern != null) && scanner.peek(endPattern)))) {
            switch (state) {
                case STATE_IDENTIFIER :
                    curKey = scanner.eat(wordPattern, "<identifier>");
                    state = STATE_EQUALS;
                    break;
                case STATE_EQUALS :
                    scanner.eat(equalsPattern, "=");
                    state = STATE_VALUE;
                    break;
                case STATE_VALUE :
                    if ((curValue = scanner.tryEat(stringPattern)) != null) {
                        newVals.put(curKey, curValue.substring(1, curValue.length() - 1));
                    } else
                        if ((curValue = scanner.tryEat(referencePattern)) != null) {
                            java.lang.String refName = curValue.substring(1, curValue.length());
                            java.lang.Object referencedObject = (mBoundReferences != null) ? mBoundReferences.get(refName) : null;
                            if (referencedObject == null) {
                                throw new android.filterfw.io.GraphIOException(("Unknown object reference to '" + refName) + "'!");
                            }
                            newVals.put(curKey, referencedObject);
                        } else
                            if ((curValue = scanner.tryEat(booleanPattern)) != null) {
                                newVals.put(curKey, java.lang.Boolean.parseBoolean(curValue));
                            } else
                                if ((curValue = scanner.tryEat(floatPattern)) != null) {
                                    newVals.put(curKey, java.lang.Float.parseFloat(curValue));
                                } else
                                    if ((curValue = scanner.tryEat(intPattern)) != null) {
                                        newVals.put(curKey, java.lang.Integer.parseInt(curValue));
                                    } else {
                                        throw new android.filterfw.io.GraphIOException(scanner.unexpectedTokenMessage("<value>"));
                                    }




                    state = STATE_POST_VALUE;
                    break;
                case STATE_POST_VALUE :
                    scanner.eat(semicolonPattern, ";");
                    state = STATE_IDENTIFIER;
                    break;
            }
        } 
        // Make sure end is expected
        if ((state != STATE_IDENTIFIER) && (state != STATE_POST_VALUE)) {
            throw new android.filterfw.io.GraphIOException(("Unexpected end of assignments on line " + scanner.lineNo()) + "!");
        }
        return newVals;
    }

    private void bindExternal(java.lang.String name) throws android.filterfw.io.GraphIOException {
        if (mReferences.containsKey(name)) {
            java.lang.Object value = mReferences.get(name);
            mBoundReferences.put(name, value);
        } else {
            throw new android.filterfw.io.GraphIOException(((("Unknown external variable '" + name) + "'! ") + "You must add a reference to this external in the host program using ") + "addReference(...)!");
        }
    }

    /**
     * Unused for now: Often you may want to declare references that are NOT in a certain graph,
     * e.g. when reading multiple graphs with the same reader. We could print a warning, but even
     * that may be too much.
     */
    private void checkReferences() throws android.filterfw.io.GraphIOException {
        for (java.lang.String reference : mReferences.keySet()) {
            if (!mBoundReferences.containsKey(reference)) {
                throw new android.filterfw.io.GraphIOException((("Host program specifies reference to '" + reference) + "', which is not ") + "declared @external in graph file!");
            }
        }
    }

    private void applySettings() throws android.filterfw.io.GraphIOException {
        for (java.lang.String setting : mSettings.keySet()) {
            java.lang.Object value = mSettings.get(setting);
            if (setting.equals("autoBranch")) {
                expectSettingClass(setting, value, java.lang.String.class);
                if (value.equals("synced")) {
                    mCurrentGraph.setAutoBranchMode(android.filterfw.core.FilterGraph.AUTOBRANCH_SYNCED);
                } else
                    if (value.equals("unsynced")) {
                        mCurrentGraph.setAutoBranchMode(android.filterfw.core.FilterGraph.AUTOBRANCH_UNSYNCED);
                    } else
                        if (value.equals("off")) {
                            mCurrentGraph.setAutoBranchMode(android.filterfw.core.FilterGraph.AUTOBRANCH_OFF);
                        } else {
                            throw new android.filterfw.io.GraphIOException(("Unknown autobranch setting: " + value) + "!");
                        }


            } else
                if (setting.equals("discardUnconnectedOutputs")) {
                    expectSettingClass(setting, value, java.lang.Boolean.class);
                    mCurrentGraph.setDiscardUnconnectedOutputs(((java.lang.Boolean) (value)));
                } else {
                    throw new android.filterfw.io.GraphIOException(("Unknown @setting '" + setting) + "'!");
                }

        }
    }

    private void expectSettingClass(java.lang.String setting, java.lang.Object value, java.lang.Class expectedClass) throws android.filterfw.io.GraphIOException {
        if (value.getClass() != expectedClass) {
            throw new android.filterfw.io.GraphIOException(((((("Setting '" + setting) + "' must have a value of type ") + expectedClass.getSimpleName()) + ", but found a value of type ") + value.getClass().getSimpleName()) + "!");
        }
    }

    private void executeCommands() throws android.filterfw.io.GraphIOException {
        for (android.filterfw.io.TextGraphReader.Command command : mCommands) {
            command.execute(this);
        }
    }
}

