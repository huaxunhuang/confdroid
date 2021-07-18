/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.media;


/**
 * MediaPlayer class can be used to control playback
 * of audio/video files and streams. An example on how to use the methods in
 * this class can be found in {@link android.widget.VideoView}.
 *
 * <p>Topics covered here are:
 * <ol>
 * <li><a href="#StateDiagram">State Diagram</a>
 * <li><a href="#Valid_and_Invalid_States">Valid and Invalid States</a>
 * <li><a href="#Permissions">Permissions</a>
 * <li><a href="#Callbacks">Register informational and error callbacks</a>
 * </ol>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about how to use MediaPlayer, read the
 * <a href="{@docRoot }guide/topics/media/mediaplayer.html">Media Playback</a> developer guide.</p>
 * </div>
 *
 * <a name="StateDiagram"></a>
 * <h3>State Diagram</h3>
 *
 * <p>Playback control of audio/video files and streams is managed as a state
 * machine. The following diagram shows the life cycle and the states of a
 * MediaPlayer object driven by the supported playback control operations.
 * The ovals represent the states a MediaPlayer object may reside
 * in. The arcs represent the playback control operations that drive the object
 * state transition. There are two types of arcs. The arcs with a single arrow
 * head represent synchronous method calls, while those with
 * a double arrow head represent asynchronous method calls.</p>
 *
 * <p><img src="../../../images/mediaplayer_state_diagram.gif"
 *         alt="MediaPlayer State diagram"
 *         border="0" /></p>
 *
 * <p>From this state diagram, one can see that a MediaPlayer object has the
 *    following states:</p>
 * <ul>
 *     <li>When a MediaPlayer object is just created using <code>new</code> or
 *         after {@link #reset()} is called, it is in the <em>Idle</em> state; and after
 *         {@link #release()} is called, it is in the <em>End</em> state. Between these
 *         two states is the life cycle of the MediaPlayer object.
 *         <ul>
 *         <li>There is a subtle but important difference between a newly constructed
 *         MediaPlayer object and the MediaPlayer object after {@link #reset()}
 *         is called. It is a programming error to invoke methods such
 *         as {@link #getCurrentPosition()},
 *         {@link #getDuration()}, {@link #getVideoHeight()},
 *         {@link #getVideoWidth()}, {@link #setAudioStreamType(int)},
 *         {@link #setLooping(boolean)},
 *         {@link #setVolume(float, float)}, {@link #pause()}, {@link #start()},
 *         {@link #stop()}, {@link #seekTo(int)}, {@link #prepare()} or
 *         {@link #prepareAsync()} in the <em>Idle</em> state for both cases. If any of these
 *         methods is called right after a MediaPlayer object is constructed,
 *         the user supplied callback method OnErrorListener.onError() won't be
 *         called by the internal player engine and the object state remains
 *         unchanged; but if these methods are called right after {@link #reset()},
 *         the user supplied callback method OnErrorListener.onError() will be
 *         invoked by the internal player engine and the object will be
 *         transfered to the <em>Error</em> state. </li>
 *         <li>It is also recommended that once
 *         a MediaPlayer object is no longer being used, call {@link #release()} immediately
 *         so that resources used by the internal player engine associated with the
 *         MediaPlayer object can be released immediately. Resource may include
 *         singleton resources such as hardware acceleration components and
 *         failure to call {@link #release()} may cause subsequent instances of
 *         MediaPlayer objects to fallback to software implementations or fail
 *         altogether. Once the MediaPlayer
 *         object is in the <em>End</em> state, it can no longer be used and
 *         there is no way to bring it back to any other state. </li>
 *         <li>Furthermore,
 *         the MediaPlayer objects created using <code>new</code> is in the
 *         <em>Idle</em> state, while those created with one
 *         of the overloaded convenient <code>create</code> methods are <em>NOT</em>
 *         in the <em>Idle</em> state. In fact, the objects are in the <em>Prepared</em>
 *         state if the creation using <code>create</code> method is successful.
 *         </li>
 *         </ul>
 *         </li>
 *     <li>In general, some playback control operation may fail due to various
 *         reasons, such as unsupported audio/video format, poorly interleaved
 *         audio/video, resolution too high, streaming timeout, and the like.
 *         Thus, error reporting and recovery is an important concern under
 *         these circumstances. Sometimes, due to programming errors, invoking a playback
 *         control operation in an invalid state may also occur. Under all these
 *         error conditions, the internal player engine invokes a user supplied
 *         OnErrorListener.onError() method if an OnErrorListener has been
 *         registered beforehand via
 *         {@link #setOnErrorListener(android.media.MediaPlayer.OnErrorListener)}.
 *         <ul>
 *         <li>It is important to note that once an error occurs, the
 *         MediaPlayer object enters the <em>Error</em> state (except as noted
 *         above), even if an error listener has not been registered by the application.</li>
 *         <li>In order to reuse a MediaPlayer object that is in the <em>
 *         Error</em> state and recover from the error,
 *         {@link #reset()} can be called to restore the object to its <em>Idle</em>
 *         state.</li>
 *         <li>It is good programming practice to have your application
 *         register a OnErrorListener to look out for error notifications from
 *         the internal player engine.</li>
 *         <li>IllegalStateException is
 *         thrown to prevent programming errors such as calling {@link #prepare()},
 *         {@link #prepareAsync()}, or one of the overloaded <code>setDataSource
 *         </code> methods in an invalid state. </li>
 *         </ul>
 *         </li>
 *     <li>Calling
 *         {@link #setDataSource(FileDescriptor)}, or
 *         {@link #setDataSource(String)}, or
 *         {@link #setDataSource(Context, Uri)}, or
 *         {@link #setDataSource(FileDescriptor, long, long)}, or
 *         {@link #setDataSource(MediaDataSource)} transfers a
 *         MediaPlayer object in the <em>Idle</em> state to the
 *         <em>Initialized</em> state.
 *         <ul>
 *         <li>An IllegalStateException is thrown if
 *         setDataSource() is called in any other state.</li>
 *         <li>It is good programming
 *         practice to always look out for <code>IllegalArgumentException</code>
 *         and <code>IOException</code> that may be thrown from the overloaded
 *         <code>setDataSource</code> methods.</li>
 *         </ul>
 *         </li>
 *     <li>A MediaPlayer object must first enter the <em>Prepared</em> state
 *         before playback can be started.
 *         <ul>
 *         <li>There are two ways (synchronous vs.
 *         asynchronous) that the <em>Prepared</em> state can be reached:
 *         either a call to {@link #prepare()} (synchronous) which
 *         transfers the object to the <em>Prepared</em> state once the method call
 *         returns, or a call to {@link #prepareAsync()} (asynchronous) which
 *         first transfers the object to the <em>Preparing</em> state after the
 *         call returns (which occurs almost right way) while the internal
 *         player engine continues working on the rest of preparation work
 *         until the preparation work completes. When the preparation completes or when {@link #prepare()} call returns,
 *         the internal player engine then calls a user supplied callback method,
 *         onPrepared() of the OnPreparedListener interface, if an
 *         OnPreparedListener is registered beforehand via {@link #setOnPreparedListener(android.media.MediaPlayer.OnPreparedListener)}.</li>
 *         <li>It is important to note that
 *         the <em>Preparing</em> state is a transient state, and the behavior
 *         of calling any method with side effect while a MediaPlayer object is
 *         in the <em>Preparing</em> state is undefined.</li>
 *         <li>An IllegalStateException is
 *         thrown if {@link #prepare()} or {@link #prepareAsync()} is called in
 *         any other state.</li>
 *         <li>While in the <em>Prepared</em> state, properties
 *         such as audio/sound volume, screenOnWhilePlaying, looping can be
 *         adjusted by invoking the corresponding set methods.</li>
 *         </ul>
 *         </li>
 *     <li>To start the playback, {@link #start()} must be called. After
 *         {@link #start()} returns successfully, the MediaPlayer object is in the
 *         <em>Started</em> state. {@link #isPlaying()} can be called to test
 *         whether the MediaPlayer object is in the <em>Started</em> state.
 *         <ul>
 *         <li>While in the <em>Started</em> state, the internal player engine calls
 *         a user supplied OnBufferingUpdateListener.onBufferingUpdate() callback
 *         method if a OnBufferingUpdateListener has been registered beforehand
 *         via {@link #setOnBufferingUpdateListener(OnBufferingUpdateListener)}.
 *         This callback allows applications to keep track of the buffering status
 *         while streaming audio/video.</li>
 *         <li>Calling {@link #start()} has not effect
 *         on a MediaPlayer object that is already in the <em>Started</em> state.</li>
 *         </ul>
 *         </li>
 *     <li>Playback can be paused and stopped, and the current playback position
 *         can be adjusted. Playback can be paused via {@link #pause()}. When the call to
 *         {@link #pause()} returns, the MediaPlayer object enters the
 *         <em>Paused</em> state. Note that the transition from the <em>Started</em>
 *         state to the <em>Paused</em> state and vice versa happens
 *         asynchronously in the player engine. It may take some time before
 *         the state is updated in calls to {@link #isPlaying()}, and it can be
 *         a number of seconds in the case of streamed content.
 *         <ul>
 *         <li>Calling {@link #start()} to resume playback for a paused
 *         MediaPlayer object, and the resumed playback
 *         position is the same as where it was paused. When the call to
 *         {@link #start()} returns, the paused MediaPlayer object goes back to
 *         the <em>Started</em> state.</li>
 *         <li>Calling {@link #pause()} has no effect on
 *         a MediaPlayer object that is already in the <em>Paused</em> state.</li>
 *         </ul>
 *         </li>
 *     <li>Calling  {@link #stop()} stops playback and causes a
 *         MediaPlayer in the <em>Started</em>, <em>Paused</em>, <em>Prepared
 *         </em> or <em>PlaybackCompleted</em> state to enter the
 *         <em>Stopped</em> state.
 *         <ul>
 *         <li>Once in the <em>Stopped</em> state, playback cannot be started
 *         until {@link #prepare()} or {@link #prepareAsync()} are called to set
 *         the MediaPlayer object to the <em>Prepared</em> state again.</li>
 *         <li>Calling {@link #stop()} has no effect on a MediaPlayer
 *         object that is already in the <em>Stopped</em> state.</li>
 *         </ul>
 *         </li>
 *     <li>The playback position can be adjusted with a call to
 *         {@link #seekTo(int)}.
 *         <ul>
 *         <li>Although the asynchronuous {@link #seekTo(int)}
 *         call returns right way, the actual seek operation may take a while to
 *         finish, especially for audio/video being streamed. When the actual
 *         seek operation completes, the internal player engine calls a user
 *         supplied OnSeekComplete.onSeekComplete() if an OnSeekCompleteListener
 *         has been registered beforehand via
 *         {@link #setOnSeekCompleteListener(OnSeekCompleteListener)}.</li>
 *         <li>Please
 *         note that {@link #seekTo(int)} can also be called in the other states,
 *         such as <em>Prepared</em>, <em>Paused</em> and <em>PlaybackCompleted
 *         </em> state.</li>
 *         <li>Furthermore, the actual current playback position
 *         can be retrieved with a call to {@link #getCurrentPosition()}, which
 *         is helpful for applications such as a Music player that need to keep
 *         track of the playback progress.</li>
 *         </ul>
 *         </li>
 *     <li>When the playback reaches the end of stream, the playback completes.
 *         <ul>
 *         <li>If the looping mode was being set to <var>true</var>with
 *         {@link #setLooping(boolean)}, the MediaPlayer object shall remain in
 *         the <em>Started</em> state.</li>
 *         <li>If the looping mode was set to <var>false
 *         </var>, the player engine calls a user supplied callback method,
 *         OnCompletion.onCompletion(), if a OnCompletionListener is registered
 *         beforehand via {@link #setOnCompletionListener(OnCompletionListener)}.
 *         The invoke of the callback signals that the object is now in the <em>
 *         PlaybackCompleted</em> state.</li>
 *         <li>While in the <em>PlaybackCompleted</em>
 *         state, calling {@link #start()} can restart the playback from the
 *         beginning of the audio/video source.</li>
 * </ul>
 *
 *
 * <a name="Valid_and_Invalid_States"></a>
 * <h3>Valid and invalid states</h3>
 *
 * <table border="0" cellspacing="0" cellpadding="0">
 * <tr><td>Method Name </p></td>
 *     <td>Valid Sates </p></td>
 *     <td>Invalid States </p></td>
 *     <td>Comments </p></td></tr>
 * <tr><td>attachAuxEffect </p></td>
 *     <td>{Initialized, Prepared, Started, Paused, Stopped, PlaybackCompleted} </p></td>
 *     <td>{Idle, Error} </p></td>
 *     <td>This method must be called after setDataSource.
 *     Calling it does not change the object state. </p></td></tr>
 * <tr><td>getAudioSessionId </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>getCurrentPosition </p></td>
 *     <td>{Idle, Initialized, Prepared, Started, Paused, Stopped,
 *         PlaybackCompleted} </p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change the
 *         state. Calling this method in an invalid state transfers the object
 *         to the <em>Error</em> state. </p></td></tr>
 * <tr><td>getDuration </p></td>
 *     <td>{Prepared, Started, Paused, Stopped, PlaybackCompleted} </p></td>
 *     <td>{Idle, Initialized, Error} </p></td>
 *     <td>Successful invoke of this method in a valid state does not change the
 *         state. Calling this method in an invalid state transfers the object
 *         to the <em>Error</em> state. </p></td></tr>
 * <tr><td>getVideoHeight </p></td>
 *     <td>{Idle, Initialized, Prepared, Started, Paused, Stopped,
 *         PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change the
 *         state. Calling this method in an invalid state transfers the object
 *         to the <em>Error</em> state.  </p></td></tr>
 * <tr><td>getVideoWidth </p></td>
 *     <td>{Idle, Initialized, Prepared, Started, Paused, Stopped,
 *         PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change
 *         the state. Calling this method in an invalid state transfers the
 *         object to the <em>Error</em> state. </p></td></tr>
 * <tr><td>isPlaying </p></td>
 *     <td>{Idle, Initialized, Prepared, Started, Paused, Stopped,
 *          PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change
 *         the state. Calling this method in an invalid state transfers the
 *         object to the <em>Error</em> state. </p></td></tr>
 * <tr><td>pause </p></td>
 *     <td>{Started, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Prepared, Stopped, Error}</p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Paused</em> state. Calling this method in an
 *         invalid state transfers the object to the <em>Error</em> state.</p></td></tr>
 * <tr><td>prepare </p></td>
 *     <td>{Initialized, Stopped} </p></td>
 *     <td>{Idle, Prepared, Started, Paused, PlaybackCompleted, Error} </p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Prepared</em> state. Calling this method in an
 *         invalid state throws an IllegalStateException.</p></td></tr>
 * <tr><td>prepareAsync </p></td>
 *     <td>{Initialized, Stopped} </p></td>
 *     <td>{Idle, Prepared, Started, Paused, PlaybackCompleted, Error} </p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Preparing</em> state. Calling this method in an
 *         invalid state throws an IllegalStateException.</p></td></tr>
 * <tr><td>release </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>After {@link #release()}, the object is no longer available. </p></td></tr>
 * <tr><td>reset </p></td>
 *     <td>{Idle, Initialized, Prepared, Started, Paused, Stopped,
 *         PlaybackCompleted, Error}</p></td>
 *     <td>{}</p></td>
 *     <td>After {@link #reset()}, the object is like being just created.</p></td></tr>
 * <tr><td>seekTo </p></td>
 *     <td>{Prepared, Started, Paused, PlaybackCompleted} </p></td>
 *     <td>{Idle, Initialized, Stopped, Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change
 *         the state. Calling this method in an invalid state transfers the
 *         object to the <em>Error</em> state. </p></td></tr>
 * <tr><td>setAudioAttributes </p></td>
 *     <td>{Idle, Initialized, Stopped, Prepared, Started, Paused,
 *          PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method does not change the state. In order for the
 *         target audio attributes type to become effective, this method must be called before
 *         prepare() or prepareAsync().</p></td></tr>
 * <tr><td>setAudioSessionId </p></td>
 *     <td>{Idle} </p></td>
 *     <td>{Initialized, Prepared, Started, Paused, Stopped, PlaybackCompleted,
 *          Error} </p></td>
 *     <td>This method must be called in idle state as the audio session ID must be known before
 *         calling setDataSource. Calling it does not change the object state. </p></td></tr>
 * <tr><td>setAudioStreamType </p></td>
 *     <td>{Idle, Initialized, Stopped, Prepared, Started, Paused,
 *          PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method does not change the state. In order for the
 *         target audio stream type to become effective, this method must be called before
 *         prepare() or prepareAsync().</p></td></tr>
 * <tr><td>setAuxEffectSendLevel </p></td>
 *     <td>any</p></td>
 *     <td>{} </p></td>
 *     <td>Calling this method does not change the object state. </p></td></tr>
 * <tr><td>setDataSource </p></td>
 *     <td>{Idle} </p></td>
 *     <td>{Initialized, Prepared, Started, Paused, Stopped, PlaybackCompleted,
 *          Error} </p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Initialized</em> state. Calling this method in an
 *         invalid state throws an IllegalStateException.</p></td></tr>
 * <tr><td>setDisplay </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setSurface </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setVideoScalingMode </p></td>
 *     <td>{Initialized, Prepared, Started, Paused, Stopped, PlaybackCompleted} </p></td>
 *     <td>{Idle, Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.</p></td></tr>
 * <tr><td>setLooping </p></td>
 *     <td>{Idle, Initialized, Stopped, Prepared, Started, Paused,
 *         PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method in a valid state does not change
 *         the state. Calling this method in an
 *         invalid state transfers the object to the <em>Error</em> state.</p></td></tr>
 * <tr><td>isLooping </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setOnBufferingUpdateListener </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setOnCompletionListener </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setOnErrorListener </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setOnPreparedListener </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setOnSeekCompleteListener </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state. </p></td></tr>
 * <tr><td>setPlaybackParams</p></td>
 *     <td>{Initialized, Prepared, Started, Paused, PlaybackCompleted, Error}</p></td>
 *     <td>{Idle, Stopped} </p></td>
 *     <td>This method will change state in some cases, depending on when it's called.
 *         </p></td></tr>
 * <tr><td>setScreenOnWhilePlaying</></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state.  </p></td></tr>
 * <tr><td>setVolume </p></td>
 *     <td>{Idle, Initialized, Stopped, Prepared, Started, Paused,
 *          PlaybackCompleted}</p></td>
 *     <td>{Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.
 * <tr><td>setWakeMode </p></td>
 *     <td>any </p></td>
 *     <td>{} </p></td>
 *     <td>This method can be called in any state and calling it does not change
 *         the object state.</p></td></tr>
 * <tr><td>start </p></td>
 *     <td>{Prepared, Started, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Stopped, Error}</p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Started</em> state. Calling this method in an
 *         invalid state transfers the object to the <em>Error</em> state.</p></td></tr>
 * <tr><td>stop </p></td>
 *     <td>{Prepared, Started, Stopped, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Error}</p></td>
 *     <td>Successful invoke of this method in a valid state transfers the
 *         object to the <em>Stopped</em> state. Calling this method in an
 *         invalid state transfers the object to the <em>Error</em> state.</p></td></tr>
 * <tr><td>getTrackInfo </p></td>
 *     <td>{Prepared, Started, Stopped, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.</p></td></tr>
 * <tr><td>addTimedTextSource </p></td>
 *     <td>{Prepared, Started, Stopped, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.</p></td></tr>
 * <tr><td>selectTrack </p></td>
 *     <td>{Prepared, Started, Stopped, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.</p></td></tr>
 * <tr><td>deselectTrack </p></td>
 *     <td>{Prepared, Started, Stopped, Paused, PlaybackCompleted}</p></td>
 *     <td>{Idle, Initialized, Error}</p></td>
 *     <td>Successful invoke of this method does not change the state.</p></td></tr>
 *
 * </table>
 *
 * <a name="Permissions"></a>
 * <h3>Permissions</h3>
 * <p>One may need to declare a corresponding WAKE_LOCK permission {@link android.R.styleable#AndroidManifestUsesPermission &lt;uses-permission&gt;}
 * element.
 *
 * <p>This class requires the {@link android.Manifest.permission#INTERNET} permission
 * when used with network-based content.
 *
 * <a name="Callbacks"></a>
 * <h3>Callbacks</h3>
 * <p>Applications may want to register for informational and error
 * events in order to be informed of some internal state update and
 * possible runtime errors during playback or streaming. Registration for
 * these events is done by properly setting the appropriate listeners (via calls
 * to
 * {@link #setOnPreparedListener(OnPreparedListener)}setOnPreparedListener,
 * {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)}setOnVideoSizeChangedListener,
 * {@link #setOnSeekCompleteListener(OnSeekCompleteListener)}setOnSeekCompleteListener,
 * {@link #setOnCompletionListener(OnCompletionListener)}setOnCompletionListener,
 * {@link #setOnBufferingUpdateListener(OnBufferingUpdateListener)}setOnBufferingUpdateListener,
 * {@link #setOnInfoListener(OnInfoListener)}setOnInfoListener,
 * {@link #setOnErrorListener(OnErrorListener)}setOnErrorListener, etc).
 * In order to receive the respective callback
 * associated with these listeners, applications are required to create
 * MediaPlayer objects on a thread with its own Looper running (main UI
 * thread by default has a Looper running).
 */
public class MediaPlayer extends android.media.PlayerBase implements android.media.SubtitleController.Listener {
    /**
     * Constant to retrieve only the new metadata since the last
     * call.
     * // FIXME: unhide.
     * // FIXME: add link to getMetadata(boolean, boolean)
     * {@hide }
     */
    public static final boolean METADATA_UPDATE_ONLY = true;

    /**
     * Constant to retrieve all the metadata.
     * // FIXME: unhide.
     * // FIXME: add link to getMetadata(boolean, boolean)
     * {@hide }
     */
    public static final boolean METADATA_ALL = false;

    /**
     * Constant to enable the metadata filter during retrieval.
     * // FIXME: unhide.
     * // FIXME: add link to getMetadata(boolean, boolean)
     * {@hide }
     */
    public static final boolean APPLY_METADATA_FILTER = true;

    /**
     * Constant to disable the metadata filter during retrieval.
     * // FIXME: unhide.
     * // FIXME: add link to getMetadata(boolean, boolean)
     * {@hide }
     */
    public static final boolean BYPASS_METADATA_FILTER = false;

    static {
        java.lang.System.loadLibrary("media_jni");
        android.media.MediaPlayer.native_init();
    }

    private static final java.lang.String TAG = "MediaPlayer";

    // Name of the remote interface for the media player. Must be kept
    // in sync with the 2nd parameter of the IMPLEMENT_META_INTERFACE
    // macro invocation in IMediaPlayer.cpp
    private static final java.lang.String IMEDIA_PLAYER = "android.media.IMediaPlayer";

    private long mNativeContext;// accessed by native methods


    private long mNativeSurfaceTexture;// accessed by native methods


    private int mListenerContext;// accessed by native methods


    private android.view.SurfaceHolder mSurfaceHolder;

    private android.media.MediaPlayer.EventHandler mEventHandler;

    private android.os.PowerManager.WakeLock mWakeLock = null;

    private boolean mScreenOnWhilePlaying;

    private boolean mStayAwake;

    private int mStreamType = android.media.AudioManager.USE_DEFAULT_STREAM_TYPE;

    private int mUsage = -1;

    private boolean mBypassInterruptionPolicy;

    /**
     * Default constructor. Consider using one of the create() methods for
     * synchronously instantiating a MediaPlayer from a Uri or resource.
     * <p>When done with the MediaPlayer, you should call  {@link #release()},
     * to free the resources. If not released, too many MediaPlayer instances may
     * result in an exception.</p>
     */
    public MediaPlayer() {
        super(new android.media.AudioAttributes.Builder().build());
        android.os.Looper looper;
        if ((looper = android.os.Looper.myLooper()) != null) {
            mEventHandler = new android.media.MediaPlayer.EventHandler(this, looper);
        } else
            if ((looper = android.os.Looper.getMainLooper()) != null) {
                mEventHandler = new android.media.MediaPlayer.EventHandler(this, looper);
            } else {
                mEventHandler = null;
            }

        mTimeProvider = new android.media.MediaPlayer.TimeProvider(this);
        mOpenSubtitleSources = new java.util.Vector<java.io.InputStream>();
        /* Native setup requires a weak reference to our object.
        It's easier to create it here than in C++.
         */
        native_setup(new java.lang.ref.WeakReference<android.media.MediaPlayer>(this));
    }

    /* Update the MediaPlayer SurfaceTexture.
    Call after setting a new display surface.
     */
    private native void _setVideoSurface(android.view.Surface surface);

    /* Do not change these values (starting with INVOKE_ID) without updating
    their counterparts in include/media/mediaplayer.h!
     */
    private static final int INVOKE_ID_GET_TRACK_INFO = 1;

    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE = 2;

    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE_FD = 3;

    private static final int INVOKE_ID_SELECT_TRACK = 4;

    private static final int INVOKE_ID_DESELECT_TRACK = 5;

    private static final int INVOKE_ID_SET_VIDEO_SCALE_MODE = 6;

    private static final int INVOKE_ID_GET_SELECTED_TRACK = 7;

    /**
     * Create a request parcel which can be routed to the native media
     * player using {@link #invoke(Parcel, Parcel)}. The Parcel
     * returned has the proper InterfaceToken set. The caller should
     * not overwrite that token, i.e it can only append data to the
     * Parcel.
     *
     * @return A parcel suitable to hold a request for the native
    player.
    {@hide }
     */
    public android.os.Parcel newRequest() {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        parcel.writeInterfaceToken(android.media.MediaPlayer.IMEDIA_PLAYER);
        return parcel;
    }

    /**
     * Invoke a generic method on the native player using opaque
     * parcels for the request and reply. Both payloads' format is a
     * convention between the java caller and the native player.
     * Must be called after setDataSource to make sure a native player
     * exists. On failure, a RuntimeException is thrown.
     *
     * @param request
     * 		Parcel with the data for the extension. The
     * 		caller must use {@link #newRequest()} to get one.
     * @param reply
     * 		Output parcel with the data returned by the
     * 		native player.
     * 		{@hide }
     */
    public void invoke(android.os.Parcel request, android.os.Parcel reply) {
        int retcode = native_invoke(request, reply);
        reply.setDataPosition(0);
        if (retcode != 0) {
            throw new java.lang.RuntimeException("failure code: " + retcode);
        }
    }

    /**
     * Sets the {@link SurfaceHolder} to use for displaying the video
     * portion of the media.
     *
     * Either a surface holder or surface must be set if a display or video sink
     * is needed.  Not calling this method or {@link #setSurface(Surface)}
     * when playing back a video will result in only the audio track being played.
     * A null surface holder or surface will result in only the audio track being
     * played.
     *
     * @param sh
     * 		the SurfaceHolder to use for video display
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized or has been released.
     */
    public void setDisplay(android.view.SurfaceHolder sh) {
        mSurfaceHolder = sh;
        android.view.Surface surface;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    /**
     * Sets the {@link Surface} to be used as the sink for the video portion of
     * the media. This is similar to {@link #setDisplay(SurfaceHolder)}, but
     * does not support {@link #setScreenOnWhilePlaying(boolean)}.  Setting a
     * Surface will un-set any Surface or SurfaceHolder that was previously set.
     * A null surface will result in only the audio track being played.
     *
     * If the Surface sends frames to a {@link SurfaceTexture}, the timestamps
     * returned from {@link SurfaceTexture#getTimestamp()} will have an
     * unspecified zero point.  These timestamps cannot be directly compared
     * between different media sources, different instances of the same media
     * source, or multiple runs of the same program.  The timestamp is normally
     * monotonically increasing and is unaffected by time-of-day adjustments,
     * but it is reset when the position is set.
     *
     * @param surface
     * 		The {@link Surface} to be used for the video portion of
     * 		the media.
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized or has been released.
     */
    public void setSurface(android.view.Surface surface) {
        if (mScreenOnWhilePlaying && (surface != null)) {
            android.util.Log.w(android.media.MediaPlayer.TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    /* Do not change these video scaling mode values below without updating
    their counterparts in system/window.h! Please do not forget to update
    {@link #isVideoScalingModeSupported} when new video scaling modes
    are added.
     */
    /**
     * Specifies a video scaling mode. The content is stretched to the
     * surface rendering area. When the surface has the same aspect ratio
     * as the content, the aspect ratio of the content is maintained;
     * otherwise, the aspect ratio of the content is not maintained when video
     * is being rendered. Unlike {@link #VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING},
     * there is no content cropping with this video scaling mode.
     */
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;

    /**
     * Specifies a video scaling mode. The content is scaled, maintaining
     * its aspect ratio. The whole surface area is always used. When the
     * aspect ratio of the content is the same as the surface, no content
     * is cropped; otherwise, content is cropped to fit the surface.
     */
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;

    /**
     * Sets video scaling mode. To make the target video scaling mode
     * effective during playback, this method must be called after
     * data source is set. If not called, the default video
     * scaling mode is {@link #VIDEO_SCALING_MODE_SCALE_TO_FIT}.
     *
     * <p> The supported video scaling modes are:
     * <ul>
     * <li> {@link #VIDEO_SCALING_MODE_SCALE_TO_FIT}
     * <li> {@link #VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING}
     * </ul>
     *
     * @param mode
     * 		target video scaling mode. Must be one of the supported
     * 		video scaling modes; otherwise, IllegalArgumentException will be thrown.
     * @see MediaPlayer#VIDEO_SCALING_MODE_SCALE_TO_FIT
     * @see MediaPlayer#VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
     */
    public void setVideoScalingMode(int mode) {
        if (!isVideoScalingModeSupported(mode)) {
            final java.lang.String msg = ("Scaling mode " + mode) + " is not supported";
            throw new java.lang.IllegalArgumentException(msg);
        }
        android.os.Parcel request = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            request.writeInterfaceToken(android.media.MediaPlayer.IMEDIA_PLAYER);
            request.writeInt(android.media.MediaPlayer.INVOKE_ID_SET_VIDEO_SCALE_MODE);
            request.writeInt(mode);
            invoke(request, reply);
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    /**
     * Convenience method to create a MediaPlayer for a given Uri.
     * On success, {@link #prepare()} will already have been called and must not be called again.
     * <p>When done with the MediaPlayer, you should call  {@link #release()},
     * to free the resources. If not released, too many MediaPlayer instances will
     * result in an exception.</p>
     * <p>Note that since {@link #prepare()} is called automatically in this method,
     * you cannot change the audio stream type (see {@link #setAudioStreamType(int)}), audio
     * session ID (see {@link #setAudioSessionId(int)}) or audio attributes
     * (see {@link #setAudioAttributes(AudioAttributes)} of the new MediaPlayer.</p>
     *
     * @param context
     * 		the Context to use
     * @param uri
     * 		the Uri from which to get the datasource
     * @return a MediaPlayer object, or null if creation failed
     */
    public static android.media.MediaPlayer create(android.content.Context context, android.net.Uri uri) {
        return android.media.MediaPlayer.create(context, uri, null);
    }

    /**
     * Convenience method to create a MediaPlayer for a given Uri.
     * On success, {@link #prepare()} will already have been called and must not be called again.
     * <p>When done with the MediaPlayer, you should call  {@link #release()},
     * to free the resources. If not released, too many MediaPlayer instances will
     * result in an exception.</p>
     * <p>Note that since {@link #prepare()} is called automatically in this method,
     * you cannot change the audio stream type (see {@link #setAudioStreamType(int)}), audio
     * session ID (see {@link #setAudioSessionId(int)}) or audio attributes
     * (see {@link #setAudioAttributes(AudioAttributes)} of the new MediaPlayer.</p>
     *
     * @param context
     * 		the Context to use
     * @param uri
     * 		the Uri from which to get the datasource
     * @param holder
     * 		the SurfaceHolder to use for displaying the video
     * @return a MediaPlayer object, or null if creation failed
     */
    public static android.media.MediaPlayer create(android.content.Context context, android.net.Uri uri, android.view.SurfaceHolder holder) {
        int s = android.media.AudioSystem.newAudioSessionId();
        return android.media.MediaPlayer.create(context, uri, holder, null, s > 0 ? s : 0);
    }

    /**
     * Same factory method as {@link #create(Context, Uri, SurfaceHolder)} but that lets you specify
     * the audio attributes and session ID to be used by the new MediaPlayer instance.
     *
     * @param context
     * 		the Context to use
     * @param uri
     * 		the Uri from which to get the datasource
     * @param holder
     * 		the SurfaceHolder to use for displaying the video, may be null.
     * @param audioAttributes
     * 		the {@link AudioAttributes} to be used by the media player.
     * @param audioSessionId
     * 		the audio session ID to be used by the media player,
     * 		see {@link AudioManager#generateAudioSessionId()} to obtain a new session.
     * @return a MediaPlayer object, or null if creation failed
     */
    public static android.media.MediaPlayer create(android.content.Context context, android.net.Uri uri, android.view.SurfaceHolder holder, android.media.AudioAttributes audioAttributes, int audioSessionId) {
        try {
            android.media.MediaPlayer mp = new android.media.MediaPlayer();
            final android.media.AudioAttributes aa = (audioAttributes != null) ? audioAttributes : new android.media.AudioAttributes.Builder().build();
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(context, uri);
            if (holder != null) {
                mp.setDisplay(holder);
            }
            mp.prepare();
            return mp;
        } catch (java.io.IOException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        } catch (java.lang.IllegalArgumentException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        } catch (java.lang.SecurityException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    // Note no convenience method to create a MediaPlayer with SurfaceTexture sink.
    /**
     * Convenience method to create a MediaPlayer for a given resource id.
     * On success, {@link #prepare()} will already have been called and must not be called again.
     * <p>When done with the MediaPlayer, you should call  {@link #release()},
     * to free the resources. If not released, too many MediaPlayer instances will
     * result in an exception.</p>
     * <p>Note that since {@link #prepare()} is called automatically in this method,
     * you cannot change the audio stream type (see {@link #setAudioStreamType(int)}), audio
     * session ID (see {@link #setAudioSessionId(int)}) or audio attributes
     * (see {@link #setAudioAttributes(AudioAttributes)} of the new MediaPlayer.</p>
     *
     * @param context
     * 		the Context to use
     * @param resid
     * 		the raw resource id (<var>R.raw.&lt;something></var>) for
     * 		the resource to use as the datasource
     * @return a MediaPlayer object, or null if creation failed
     */
    public static android.media.MediaPlayer create(android.content.Context context, int resid) {
        int s = android.media.AudioSystem.newAudioSessionId();
        return android.media.MediaPlayer.create(context, resid, null, s > 0 ? s : 0);
    }

    /**
     * Same factory method as {@link #create(Context, int)} but that lets you specify the audio
     * attributes and session ID to be used by the new MediaPlayer instance.
     *
     * @param context
     * 		the Context to use
     * @param resid
     * 		the raw resource id (<var>R.raw.&lt;something></var>) for
     * 		the resource to use as the datasource
     * @param audioAttributes
     * 		the {@link AudioAttributes} to be used by the media player.
     * @param audioSessionId
     * 		the audio session ID to be used by the media player,
     * 		see {@link AudioManager#generateAudioSessionId()} to obtain a new session.
     * @return a MediaPlayer object, or null if creation failed
     */
    public static android.media.MediaPlayer create(android.content.Context context, int resid, android.media.AudioAttributes audioAttributes, int audioSessionId) {
        try {
            android.content.res.AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null)
                return null;

            android.media.MediaPlayer mp = new android.media.MediaPlayer();
            final android.media.AudioAttributes aa = (audioAttributes != null) ? audioAttributes : new android.media.AudioAttributes.Builder().build();
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (java.io.IOException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        } catch (java.lang.IllegalArgumentException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        } catch (java.lang.SecurityException ex) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    /**
     * Sets the data source as a content Uri.
     *
     * @param context
     * 		the Context to use when resolving the Uri
     * @param uri
     * 		the Content URI of the data you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public void setDataSource(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.net.Uri uri) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException {
        setDataSource(context, uri, null);
    }

    /**
     * Sets the data source as a content Uri.
     *
     * @param context
     * 		the Context to use when resolving the Uri
     * @param uri
     * 		the Content URI of the data you want to play
     * @param headers
     * 		the headers to be sent together with the request for the data
     * 		Note that the cross domain redirection is allowed by default, but that can be
     * 		changed with key/value pairs through the headers parameter with
     * 		"android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     * 		to disallow or allow cross domain redirection.
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public void setDataSource(@android.annotation.NonNull
    android.content.Context context, @android.annotation.NonNull
    android.net.Uri uri, @android.annotation.Nullable
    java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException {
        final android.content.ContentResolver resolver = context.getContentResolver();
        final java.lang.String scheme = uri.getScheme();
        if (android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
            setDataSource(uri.getPath());
            return;
        } else
            if (android.content.ContentResolver.SCHEME_CONTENT.equals(scheme) && android.provider.Settings.AUTHORITY.equals(uri.getAuthority())) {
                // Try cached ringtone first since the actual provider may not be
                // encryption aware, or it may be stored on CE media storage
                final int type = android.media.RingtoneManager.getDefaultType(uri);
                final android.net.Uri cacheUri = android.media.RingtoneManager.getCacheForType(type);
                final android.net.Uri actualUri = android.media.RingtoneManager.getActualDefaultRingtoneUri(context, type);
                if (attemptDataSource(resolver, cacheUri)) {
                    return;
                } else
                    if (attemptDataSource(resolver, actualUri)) {
                        return;
                    } else {
                        setDataSource(uri.toString(), headers);
                    }

            } else {
                // Try requested Uri locally first, or fallback to media server
                if (attemptDataSource(resolver, uri)) {
                    return;
                } else {
                    setDataSource(uri.toString(), headers);
                }
            }

    }

    private boolean attemptDataSource(android.content.ContentResolver resolver, android.net.Uri uri) {
        try (android.content.res.AssetFileDescriptor afd = resolver.openAssetFileDescriptor(uri, "r")) {
            setDataSource(afd);
            return true;
        } catch (java.lang.NullPointerException | java.lang.SecurityException | java.io.IOException ex) {
            android.util.Log.w(android.media.MediaPlayer.TAG, (("Couldn't open " + uri) + ": ") + ex);
            return false;
        }
    }

    /**
     * Sets the data source (file-path or http/rtsp URL) to use.
     *
     * @param path
     * 		the path of the file, or the http/rtsp URL of the stream you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * 		
     * 		<p>When <code>path</code> refers to a local file, the file may actually be opened by a
     * 		process other than the calling application.  This implies that the pathname
     * 		should be an absolute path (as any other process runs with unspecified current working
     * 		directory), and that the pathname should reference a world-readable file.
     * 		As an alternative, the application could first open the file for reading,
     * 		and then use the file descriptor form {@link #setDataSource(FileDescriptor)}.
     */
    public void setDataSource(java.lang.String path) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException {
        setDataSource(path, null, null);
    }

    /**
     * Sets the data source (file-path or http/rtsp URL) to use.
     *
     * @param path
     * 		the path of the file, or the http/rtsp URL of the stream you want to play
     * @param headers
     * 		the headers associated with the http request for the stream you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @unknown pending API council
     */
    public void setDataSource(java.lang.String path, java.util.Map<java.lang.String, java.lang.String> headers) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException {
        java.lang.String[] keys = null;
        java.lang.String[] values = null;
        if (headers != null) {
            keys = new java.lang.String[headers.size()];
            values = new java.lang.String[headers.size()];
            int i = 0;
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : headers.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                ++i;
            }
        }
        setDataSource(path, keys, values);
    }

    private void setDataSource(java.lang.String path, java.lang.String[] keys, java.lang.String[] values) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException {
        final android.net.Uri uri = android.net.Uri.parse(path);
        final java.lang.String scheme = uri.getScheme();
        if ("file".equals(scheme)) {
            path = uri.getPath();
        } else
            if (scheme != null) {
                // handle non-file sources
                nativeSetDataSource(android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, keys, values);
                return;
            }

        final java.io.File file = new java.io.File(path);
        if (file.exists()) {
            java.io.FileInputStream is = new java.io.FileInputStream(file);
            java.io.FileDescriptor fd = is.getFD();
            setDataSource(fd);
            is.close();
        } else {
            throw new java.io.IOException("setDataSource failed.");
        }
    }

    private native void nativeSetDataSource(android.os.IBinder httpServiceBinder, java.lang.String path, java.lang.String[] keys, java.lang.String[] values) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException, java.lang.SecurityException;

    /**
     * Sets the data source (AssetFileDescriptor) to use. It is the caller's
     * responsibility to close the file descriptor. It is safe to do so as soon
     * as this call returns.
     *
     * @param afd
     * 		the AssetFileDescriptor for the file you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @throws IllegalArgumentException
     * 		if afd is not a valid AssetFileDescriptor
     * @throws IOException
     * 		if afd can not be read
     */
    public void setDataSource(@android.annotation.NonNull
    android.content.res.AssetFileDescriptor afd) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        com.android.internal.util.Preconditions.checkNotNull(afd);
        // Note: using getDeclaredLength so that our behavior is the same
        // as previous versions when the content provider is returning
        // a full file.
        if (afd.getDeclaredLength() < 0) {
            setDataSource(afd.getFileDescriptor());
        } else {
            setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
        }
    }

    /**
     * Sets the data source (FileDescriptor) to use. It is the caller's responsibility
     * to close the file descriptor. It is safe to do so as soon as this call returns.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @throws IllegalArgumentException
     * 		if fd is not a valid FileDescriptor
     * @throws IOException
     * 		if fd can not be read
     */
    public void setDataSource(java.io.FileDescriptor fd) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        // intentionally less than LONG_MAX
        setDataSource(fd, 0, 0x7ffffffffffffffL);
    }

    /**
     * Sets the data source (FileDescriptor) to use.  The FileDescriptor must be
     * seekable (N.B. a LocalSocket is not seekable). It is the caller's responsibility
     * to close the file descriptor. It is safe to do so as soon as this call returns.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to play
     * @param offset
     * 		the offset into the file where the data to be played starts, in bytes
     * @param length
     * 		the length in bytes of the data to be played
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @throws IllegalArgumentException
     * 		if fd is not a valid FileDescriptor
     * @throws IOException
     * 		if fd can not be read
     */
    public void setDataSource(java.io.FileDescriptor fd, long offset, long length) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        _setDataSource(fd, offset, length);
    }

    private native void _setDataSource(java.io.FileDescriptor fd, long offset, long length) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException;

    /**
     * Sets the data source (MediaDataSource) to use.
     *
     * @param dataSource
     * 		the MediaDataSource for the media you want to play
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @throws IllegalArgumentException
     * 		if dataSource is not a valid MediaDataSource
     */
    public void setDataSource(android.media.MediaDataSource dataSource) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        _setDataSource(dataSource);
    }

    private native void _setDataSource(android.media.MediaDataSource dataSource) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException;

    /**
     * Prepares the player for playback, synchronously.
     *
     * After setting the datasource and the display surface, you need to either
     * call prepare() or prepareAsync(). For files, it is OK to call prepare(),
     * which blocks until MediaPlayer is ready for playback.
     *
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public void prepare() throws java.io.IOException, java.lang.IllegalStateException {
        _prepare();
        scanInternalSubtitleTracks();
    }

    private native void _prepare() throws java.io.IOException, java.lang.IllegalStateException;

    /**
     * Prepares the player for playback, asynchronously.
     *
     * After setting the datasource and the display surface, you need to either
     * call prepare() or prepareAsync(). For streams, you should call prepareAsync(),
     * which returns immediately, rather than blocking until enough data has been
     * buffered.
     *
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public native void prepareAsync() throws java.lang.IllegalStateException;

    /**
     * Starts or resumes playback. If playback had previously been paused,
     * playback will continue from where it was paused. If playback had
     * been stopped, or never started before, playback will start at the
     * beginning.
     *
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public void start() throws java.lang.IllegalStateException {
        baseStart();
        stayAwake(true);
        _start();
    }

    private native void _start() throws java.lang.IllegalStateException;

    private int getAudioStreamType() {
        if (mStreamType == android.media.AudioManager.USE_DEFAULT_STREAM_TYPE) {
            mStreamType = _getAudioStreamType();
        }
        return mStreamType;
    }

    private native int _getAudioStreamType() throws java.lang.IllegalStateException;

    /**
     * Stops playback after playback has been stopped or paused.
     *
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     */
    public void stop() throws java.lang.IllegalStateException {
        stayAwake(false);
        _stop();
    }

    private native void _stop() throws java.lang.IllegalStateException;

    /**
     * Pauses playback. Call start() to resume.
     *
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     */
    public void pause() throws java.lang.IllegalStateException {
        stayAwake(false);
        _pause();
    }

    private native void _pause() throws java.lang.IllegalStateException;

    /**
     * Set the low-level power management behavior for this MediaPlayer.  This
     * can be used when the MediaPlayer is not playing through a SurfaceHolder
     * set with {@link #setDisplay(SurfaceHolder)} and thus can use the
     * high-level {@link #setScreenOnWhilePlaying(boolean)} feature.
     *
     * <p>This function has the MediaPlayer access the low-level power manager
     * service to control the device's power usage while playing is occurring.
     * The parameter is a combination of {@link android.os.PowerManager} wake flags.
     * Use of this method requires {@link android.Manifest.permission#WAKE_LOCK}
     * permission.
     * By default, no attempt is made to keep the device awake during playback.
     *
     * @param context
     * 		the Context to use
     * @param mode
     * 		the power/wake mode to set
     * @see android.os.PowerManager
     */
    public void setWakeMode(android.content.Context context, int mode) {
        boolean washeld = false;
        /* Disable persistant wakelocks in media player based on property */
        if (android.os.SystemProperties.getBoolean("audio.offload.ignore_setawake", false) == true) {
            android.util.Log.w(android.media.MediaPlayer.TAG, "IGNORING setWakeMode " + mode);
            return;
        }
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                washeld = true;
                mWakeLock.release();
            }
            mWakeLock = null;
        }
        android.os.PowerManager pm = ((android.os.PowerManager) (context.getSystemService(android.content.Context.POWER_SERVICE)));
        mWakeLock = pm.newWakeLock(mode | android.os.PowerManager.ON_AFTER_RELEASE, android.media.MediaPlayer.class.getName());
        mWakeLock.setReferenceCounted(false);
        if (washeld) {
            mWakeLock.acquire();
        }
    }

    /**
     * Control whether we should use the attached SurfaceHolder to keep the
     * screen on while video playback is occurring.  This is the preferred
     * method over {@link #setWakeMode} where possible, since it doesn't
     * require that the application have permission for low-level wake lock
     * access.
     *
     * @param screenOn
     * 		Supply true to keep the screen on, false to allow it
     * 		to turn off.
     */
    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (mScreenOnWhilePlaying != screenOn) {
            if (screenOn && (mSurfaceHolder == null)) {
                android.util.Log.w(android.media.MediaPlayer.TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    private void stayAwake(boolean awake) {
        if (mWakeLock != null) {
            if (awake && (!mWakeLock.isHeld())) {
                mWakeLock.acquire();
            } else
                if ((!awake) && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }

        }
        mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (mSurfaceHolder != null) {
            mSurfaceHolder.setKeepScreenOn(mScreenOnWhilePlaying && mStayAwake);
        }
    }

    /**
     * Returns the width of the video.
     *
     * @return the width of the video, or 0 if there is no video,
    no display surface was set, or the width has not been determined
    yet. The OnVideoSizeChangedListener can be registered via
    {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)}
    to provide a notification when the width is available.
     */
    public native int getVideoWidth();

    /**
     * Returns the height of the video.
     *
     * @return the height of the video, or 0 if there is no video,
    no display surface was set, or the height has not been determined
    yet. The OnVideoSizeChangedListener can be registered via
    {@link #setOnVideoSizeChangedListener(OnVideoSizeChangedListener)}
    to provide a notification when the height is available.
     */
    public native int getVideoHeight();

    /**
     * Checks whether the MediaPlayer is playing.
     *
     * @return true if currently playing, false otherwise
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized or has been released.
     */
    public native boolean isPlaying();

    /**
     * Change playback speed of audio by resampling the audio.
     * <p>
     * Specifies resampling as audio mode for variable rate playback, i.e.,
     * resample the waveform based on the requested playback rate to get
     * a new waveform, and play back the new waveform at the original sampling
     * frequency.
     * When rate is larger than 1.0, pitch becomes higher.
     * When rate is smaller than 1.0, pitch becomes lower.
     *
     * @unknown 
     */
    public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;

    /**
     * Change playback speed of audio without changing its pitch.
     * <p>
     * Specifies time stretching as audio mode for variable rate playback.
     * Time stretching changes the duration of the audio samples without
     * affecting its pitch.
     * <p>
     * This mode is only supported for a limited range of playback speed factors,
     * e.g. between 1/2x and 2x.
     *
     * @unknown 
     */
    public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;

    /**
     * Change playback speed of audio without changing its pitch, and
     * possibly mute audio if time stretching is not supported for the playback
     * speed.
     * <p>
     * Try to keep audio pitch when changing the playback rate, but allow the
     * system to determine how to change audio playback if the rate is out
     * of range.
     *
     * @unknown 
     */
    public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_DEFAULT, android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_STRETCH, android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_RESAMPLE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PlaybackRateAudioMode {}

    /**
     * Sets playback rate and audio mode.
     *
     * @param rate
     * 		the ratio between desired playback rate and normal one.
     * @param audioMode
     * 		audio playback mode. Must be one of the supported
     * 		audio modes.
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     * @throws IllegalArgumentException
     * 		if audioMode is not supported.
     * @unknown 
     */
    @android.annotation.NonNull
    public android.media.PlaybackParams easyPlaybackParams(float rate, @android.media.MediaPlayer.PlaybackRateAudioMode
    int audioMode) {
        android.media.PlaybackParams params = new android.media.PlaybackParams();
        params.allowDefaults();
        switch (audioMode) {
            case android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_DEFAULT :
                params.setSpeed(rate).setPitch(1.0F);
                break;
            case android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_STRETCH :
                params.setSpeed(rate).setPitch(1.0F).setAudioFallbackMode(params.AUDIO_FALLBACK_MODE_FAIL);
                break;
            case android.media.MediaPlayer.PLAYBACK_RATE_AUDIO_MODE_RESAMPLE :
                params.setSpeed(rate).setPitch(rate);
                break;
            default :
                final java.lang.String msg = ("Audio playback mode " + audioMode) + " is not supported";
                throw new java.lang.IllegalArgumentException(msg);
        }
        return params;
    }

    /**
     * Sets playback rate using {@link PlaybackParams}. The object sets its internal
     * PlaybackParams to the input, except that the object remembers previous speed
     * when input speed is zero. This allows the object to resume at previous speed
     * when start() is called. Calling it before the object is prepared does not change
     * the object state. After the object is prepared, calling it with zero speed is
     * equivalent to calling pause(). After the object is prepared, calling it with
     * non-zero speed is equivalent to calling start().
     *
     * @param params
     * 		the playback params.
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized or has been released.
     * @throws IllegalArgumentException
     * 		if params is not supported.
     */
    public native void setPlaybackParams(@android.annotation.NonNull
    android.media.PlaybackParams params);

    /**
     * Gets the playback params, containing the current playback rate.
     *
     * @return the playback params.
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     */
    @android.annotation.NonNull
    public native android.media.PlaybackParams getPlaybackParams();

    /**
     * Sets A/V sync mode.
     *
     * @param params
     * 		the A/V sync params to apply
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     * @throws IllegalArgumentException
     * 		if params are not supported.
     */
    public native void setSyncParams(@android.annotation.NonNull
    android.media.SyncParams params);

    /**
     * Gets the A/V sync mode.
     *
     * @return the A/V sync params
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized.
     */
    @android.annotation.NonNull
    public native android.media.SyncParams getSyncParams();

    /**
     * Seeks to specified time position.
     *
     * @param msec
     * 		the offset in milliseconds from the start to seek to
     * @throws IllegalStateException
     * 		if the internal player engine has not been
     * 		initialized
     */
    public native void seekTo(int msec) throws java.lang.IllegalStateException;

    /**
     * Get current playback position as a {@link MediaTimestamp}.
     * <p>
     * The MediaTimestamp represents how the media time correlates to the system time in
     * a linear fashion using an anchor and a clock rate. During regular playback, the media
     * time moves fairly constantly (though the anchor frame may be rebased to a current
     * system time, the linear correlation stays steady). Therefore, this method does not
     * need to be called often.
     * <p>
     * To help users get current playback position, this method always anchors the timestamp
     * to the current {@link System#nanoTime system time}, so
     * {@link MediaTimestamp#getAnchorMediaTimeUs} can be used as current playback position.
     *
     * @return a MediaTimestamp object if a timestamp is available, or {@code null} if no timestamp
    is available, e.g. because the media player has not been initialized.
     * @see MediaTimestamp
     */
    @android.annotation.Nullable
    public android.media.MediaTimestamp getTimestamp() {
        try {
            // TODO: get the timestamp from native side
            return new android.media.MediaTimestamp(getCurrentPosition() * 1000L, java.lang.System.nanoTime(), isPlaying() ? getPlaybackParams().getSpeed() : 0.0F);
        } catch (java.lang.IllegalStateException e) {
            return null;
        }
    }

    /**
     * Gets the current playback position.
     *
     * @return the current position in milliseconds
     */
    public native int getCurrentPosition();

    /**
     * Gets the duration of the file.
     *
     * @return the duration in milliseconds, if no duration is available
    (for example, if streaming live content), -1 is returned.
     */
    public native int getDuration();

    /**
     * Gets the media metadata.
     *
     * @param update_only
     * 		controls whether the full set of available
     * 		metadata is returned or just the set that changed since the
     * 		last call. See {@see #METADATA_UPDATE_ONLY} and {@see #METADATA_ALL}.
     * @param apply_filter
     * 		if true only metadata that matches the
     * 		filter is returned. See {@see #APPLY_METADATA_FILTER} and {@see #BYPASS_METADATA_FILTER}.
     * @return The metadata, possibly empty. null if an error occured.
    // FIXME: unhide.
    {@hide }
     */
    public android.media.Metadata getMetadata(final boolean update_only, final boolean apply_filter) {
        android.os.Parcel reply = android.os.Parcel.obtain();
        android.media.Metadata data = new android.media.Metadata();
        if (!native_getMetadata(update_only, apply_filter, reply)) {
            reply.recycle();
            return null;
        }
        // Metadata takes over the parcel, don't recycle it unless
        // there is an error.
        if (!data.parse(reply)) {
            reply.recycle();
            return null;
        }
        return data;
    }

    /**
     * Set a filter for the metadata update notification and update
     * retrieval. The caller provides 2 set of metadata keys, allowed
     * and blocked. The blocked set always takes precedence over the
     * allowed one.
     * Metadata.MATCH_ALL and Metadata.MATCH_NONE are 2 sets available as
     * shorthands to allow/block all or no metadata.
     *
     * By default, there is no filter set.
     *
     * @param allow
     * 		Is the set of metadata the client is interested
     * 		in receiving new notifications for.
     * @param block
     * 		Is the set of metadata the client is not interested
     * 		in receiving new notifications for.
     * @return The call status code.

    // FIXME: unhide.
    {@hide }
     */
    public int setMetadataFilter(java.util.Set<java.lang.Integer> allow, java.util.Set<java.lang.Integer> block) {
        // Do our serialization manually instead of calling
        // Parcel.writeArray since the sets are made of the same type
        // we avoid paying the price of calling writeValue (used by
        // writeArray) which burns an extra int per element to encode
        // the type.
        android.os.Parcel request = newRequest();
        // The parcel starts already with an interface token. There
        // are 2 filters. Each one starts with a 4bytes number to
        // store the len followed by a number of int (4 bytes as well)
        // representing the metadata type.
        int capacity = request.dataSize() + (4 * (((1 + allow.size()) + 1) + block.size()));
        if (request.dataCapacity() < capacity) {
            request.setDataCapacity(capacity);
        }
        request.writeInt(allow.size());
        for (java.lang.Integer t : allow) {
            request.writeInt(t);
        }
        request.writeInt(block.size());
        for (java.lang.Integer t : block) {
            request.writeInt(t);
        }
        return native_setMetadataFilter(request);
    }

    /**
     * Set the MediaPlayer to start when this MediaPlayer finishes playback
     * (i.e. reaches the end of the stream).
     * The media framework will attempt to transition from this player to
     * the next as seamlessly as possible. The next player can be set at
     * any time before completion, but shall be after setDataSource has been
     * called successfully. The next player must be prepared by the
     * app, and the application should not call start() on it.
     * The next MediaPlayer must be different from 'this'. An exception
     * will be thrown if next == this.
     * The application may call setNextMediaPlayer(null) to indicate no
     * next player should be started at the end of playback.
     * If the current player is looping, it will keep looping and the next
     * player will not be started.
     *
     * @param next
     * 		the player to start after this one completes playback.
     */
    public native void setNextMediaPlayer(android.media.MediaPlayer next);

    /**
     * Releases resources associated with this MediaPlayer object.
     * It is considered good practice to call this method when you're
     * done using the MediaPlayer. In particular, whenever an Activity
     * of an application is paused (its onPause() method is called),
     * or stopped (its onStop() method is called), this method should be
     * invoked to release the MediaPlayer object, unless the application
     * has a special need to keep the object around. In addition to
     * unnecessary resources (such as memory and instances of codecs)
     * being held, failure to call this method immediately if a
     * MediaPlayer object is no longer needed may also lead to
     * continuous battery consumption for mobile devices, and playback
     * failure for other applications if no multiple instances of the
     * same codec are supported on a device. Even if multiple instances
     * of the same codec are supported, some performance degradation
     * may be expected when unnecessary multiple instances are used
     * at the same time.
     */
    public void release() {
        baseRelease();
        stayAwake(false);
        updateSurfaceScreenOn();
        mOnPreparedListener = null;
        mOnBufferingUpdateListener = null;
        mOnCompletionListener = null;
        mOnSeekCompleteListener = null;
        mOnErrorListener = null;
        mOnInfoListener = null;
        mOnVideoSizeChangedListener = null;
        mOnTimedTextListener = null;
        if (mTimeProvider != null) {
            mTimeProvider.close();
            mTimeProvider = null;
        }
        mOnSubtitleDataListener = null;
        _release();
    }

    private native void _release();

    /**
     * Resets the MediaPlayer to its uninitialized state. After calling
     * this method, you will have to initialize it again by setting the
     * data source and calling prepare().
     */
    public void reset() {
        mSelectedSubtitleTrackIndex = -1;
        synchronized(mOpenSubtitleSources) {
            for (final java.io.InputStream is : mOpenSubtitleSources) {
                try {
                    is.close();
                } catch (java.io.IOException e) {
                }
            }
            mOpenSubtitleSources.clear();
        }
        if (mSubtitleController != null) {
            mSubtitleController.reset();
        }
        if (mTimeProvider != null) {
            mTimeProvider.close();
            mTimeProvider = null;
        }
        stayAwake(false);
        _reset();
        // make sure none of the listeners get called anymore
        if (mEventHandler != null) {
            mEventHandler.removeCallbacksAndMessages(null);
        }
        synchronized(mIndexTrackPairs) {
            mIndexTrackPairs.clear();
            mInbandTrackIndices.clear();
        }
    }

    private native void _reset();

    /**
     * Sets the audio stream type for this MediaPlayer. See {@link AudioManager}
     * for a list of stream types. Must call this method before prepare() or
     * prepareAsync() in order for the target stream type to become effective
     * thereafter.
     *
     * @param streamtype
     * 		the audio stream type
     * @see android.media.AudioManager
     */
    public void setAudioStreamType(int streamtype) {
        baseUpdateAudioAttributes(new android.media.AudioAttributes.Builder().setInternalLegacyStreamType(streamtype).build());
        _setAudioStreamType(streamtype);
        mStreamType = streamtype;
    }

    private native void _setAudioStreamType(int streamtype);

    // Keep KEY_PARAMETER_* in sync with include/media/mediaplayer.h
    private static final int KEY_PARAMETER_AUDIO_ATTRIBUTES = 1400;

    /**
     * Sets the parameter indicated by key.
     *
     * @param key
     * 		key indicates the parameter to be set.
     * @param value
     * 		value of the parameter to be set.
     * @return true if the parameter is set successfully, false otherwise
    {@hide }
     */
    private native boolean setParameter(int key, android.os.Parcel value);

    /**
     * Sets the audio attributes for this MediaPlayer.
     * See {@link AudioAttributes} for how to build and configure an instance of this class.
     * You must call this method before {@link #prepare()} or {@link #prepareAsync()} in order
     * for the audio attributes to become effective thereafter.
     *
     * @param attributes
     * 		a non-null set of audio attributes
     */
    public void setAudioAttributes(android.media.AudioAttributes attributes) throws java.lang.IllegalArgumentException {
        if (attributes == null) {
            final java.lang.String msg = "Cannot set AudioAttributes to null";
            throw new java.lang.IllegalArgumentException(msg);
        }
        baseUpdateAudioAttributes(attributes);
        mUsage = attributes.getUsage();
        mBypassInterruptionPolicy = (attributes.getAllFlags() & android.media.AudioAttributes.FLAG_BYPASS_INTERRUPTION_POLICY) != 0;
        android.os.Parcel pattributes = android.os.Parcel.obtain();
        attributes.writeToParcel(pattributes, android.media.AudioAttributes.FLATTEN_TAGS);
        setParameter(android.media.MediaPlayer.KEY_PARAMETER_AUDIO_ATTRIBUTES, pattributes);
        pattributes.recycle();
    }

    /**
     * Sets the player to be looping or non-looping.
     *
     * @param looping
     * 		whether to loop or not
     */
    public native void setLooping(boolean looping);

    /**
     * Checks whether the MediaPlayer is looping or non-looping.
     *
     * @return true if the MediaPlayer is currently looping, false otherwise
     */
    public native boolean isLooping();

    /**
     * Sets the volume on this player.
     * This API is recommended for balancing the output of audio streams
     * within an application. Unless you are writing an application to
     * control user settings, this API should be used in preference to
     * {@link AudioManager#setStreamVolume(int, int, int)} which sets the volume of ALL streams of
     * a particular type. Note that the passed volume values are raw scalars in range 0.0 to 1.0.
     * UI controls should be scaled logarithmically.
     *
     * @param leftVolume
     * 		left volume scalar
     * @param rightVolume
     * 		right volume scalar
     */
    /* FIXME: Merge this into javadoc comment above when setVolume(float) is not @hide.
    The single parameter form below is preferred if the channel volumes don't need
    to be set independently.
     */
    public void setVolume(float leftVolume, float rightVolume) {
        baseSetVolume(leftVolume, rightVolume);
    }

    @java.lang.Override
    void playerSetVolume(float leftVolume, float rightVolume) {
        _setVolume(leftVolume, rightVolume);
    }

    private native void _setVolume(float leftVolume, float rightVolume);

    /**
     * Similar, excepts sets volume of all channels to same value.
     *
     * @unknown 
     */
    public void setVolume(float volume) {
        setVolume(volume, volume);
    }

    /**
     * Sets the audio session ID.
     *
     * @param sessionId
     * 		the audio session ID.
     * 		The audio session ID is a system wide unique identifier for the audio stream played by
     * 		this MediaPlayer instance.
     * 		The primary use of the audio session ID  is to associate audio effects to a particular
     * 		instance of MediaPlayer: if an audio session ID is provided when creating an audio effect,
     * 		this effect will be applied only to the audio content of media players within the same
     * 		audio session and not to the output mix.
     * 		When created, a MediaPlayer instance automatically generates its own audio session ID.
     * 		However, it is possible to force this player to be part of an already existing audio session
     * 		by calling this method.
     * 		This method must be called before one of the overloaded <code> setDataSource </code> methods.
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     */
    public native void setAudioSessionId(int sessionId) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException;

    /**
     * Returns the audio session ID.
     *
     * @return the audio session ID. {@see #setAudioSessionId(int)}
    Note that the audio session ID is 0 only if a problem occured when the MediaPlayer was contructed.
     */
    public native int getAudioSessionId();

    /**
     * Attaches an auxiliary effect to the player. A typical auxiliary effect is a reverberation
     * effect which can be applied on any sound source that directs a certain amount of its
     * energy to this effect. This amount is defined by setAuxEffectSendLevel().
     * See {@link #setAuxEffectSendLevel(float)}.
     * <p>After creating an auxiliary effect (e.g.
     * {@link android.media.audiofx.EnvironmentalReverb}), retrieve its ID with
     * {@link android.media.audiofx.AudioEffect#getId()} and use it when calling this method
     * to attach the player to the effect.
     * <p>To detach the effect from the player, call this method with a null effect id.
     * <p>This method must be called after one of the overloaded <code> setDataSource </code>
     * methods.
     *
     * @param effectId
     * 		system wide unique id of the effect to attach
     */
    public native void attachAuxEffect(int effectId);

    /**
     * Sets the send level of the player to the attached auxiliary effect.
     * See {@link #attachAuxEffect(int)}. The level value range is 0 to 1.0.
     * <p>By default the send level is 0, so even if an effect is attached to the player
     * this method must be called for the effect to be applied.
     * <p>Note that the passed level value is a raw scalar. UI controls should be scaled
     * logarithmically: the gain applied by audio framework ranges from -72dB to 0dB,
     * so an appropriate conversion from linear UI input x to level is:
     * x == 0 -> level = 0
     * 0 < x <= R -> level = 10^(72*(x-R)/20/R)
     *
     * @param level
     * 		send level scalar
     */
    public void setAuxEffectSendLevel(float level) {
        baseSetAuxEffectSendLevel(level);
    }

    @java.lang.Override
    int playerSetAuxEffectSendLevel(float level) {
        _setAuxEffectSendLevel(level);
        return android.media.AudioSystem.SUCCESS;
    }

    private native void _setAuxEffectSendLevel(float level);

    /* @param request Parcel destinated to the media player. The
                   Interface token must be set to the IMediaPlayer
                   one to be routed correctly through the system.
    @param reply[out] Parcel that will contain the reply.
    @return The status code.
     */
    private final native int native_invoke(android.os.Parcel request, android.os.Parcel reply);

    /* @param update_only If true fetch only the set of metadata that have
                       changed since the last invocation of getMetadata.
                       The set is built using the unfiltered
                       notifications the native player sent to the
                       MediaPlayerService during that period of
                       time. If false, all the metadatas are considered.
    @param apply_filter  If true, once the metadata set has been built based on
                        the value update_only, the current filter is applied.
    @param reply[out] On return contains the serialized
                      metadata. Valid only if the call was successful.
    @return The status code.
     */
    private final native boolean native_getMetadata(boolean update_only, boolean apply_filter, android.os.Parcel reply);

    /* @param request Parcel with the 2 serialized lists of allowed
                   metadata types followed by the one to be
                   dropped. Each list starts with an integer
                   indicating the number of metadata type elements.
    @return The status code.
     */
    private final native int native_setMetadataFilter(android.os.Parcel request);

    private static final native void native_init();

    private final native void native_setup(java.lang.Object mediaplayer_this);

    private final native void native_finalize();

    /**
     * Class for MediaPlayer to return each audio/video/subtitle track's metadata.
     *
     * @see android.media.MediaPlayer#getTrackInfo
     */
    public static class TrackInfo implements android.os.Parcelable {
        /**
         * Gets the track type.
         *
         * @return TrackType which indicates if the track is video, audio, timed text.
         */
        public int getTrackType() {
            return mTrackType;
        }

        /**
         * Gets the language code of the track.
         *
         * @return a language code in either way of ISO-639-1 or ISO-639-2.
        When the language is unknown or could not be determined,
        ISO-639-2 language code, "und", is returned.
         */
        public java.lang.String getLanguage() {
            java.lang.String language = mFormat.getString(android.media.MediaFormat.KEY_LANGUAGE);
            return language == null ? "und" : language;
        }

        /**
         * Gets the {@link MediaFormat} of the track.  If the format is
         * unknown or could not be determined, null is returned.
         */
        public android.media.MediaFormat getFormat() {
            if ((mTrackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) || (mTrackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE)) {
                return mFormat;
            }
            return null;
        }

        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;

        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;

        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;

        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;

        public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;

        public static final int MEDIA_TRACK_TYPE_METADATA = 5;

        final int mTrackType;

        final android.media.MediaFormat mFormat;

        TrackInfo(android.os.Parcel in) {
            mTrackType = in.readInt();
            // TODO: parcel in the full MediaFormat; currently we are using createSubtitleFormat
            // even for audio/video tracks, meaning we only set the mime and language.
            java.lang.String mime = in.readString();
            java.lang.String language = in.readString();
            mFormat = android.media.MediaFormat.createSubtitleFormat(mime, language);
            if (mTrackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE) {
                mFormat.setInteger(android.media.MediaFormat.KEY_IS_AUTOSELECT, in.readInt());
                mFormat.setInteger(android.media.MediaFormat.KEY_IS_DEFAULT, in.readInt());
                mFormat.setInteger(android.media.MediaFormat.KEY_IS_FORCED_SUBTITLE, in.readInt());
            }
        }

        /**
         *
         *
         * @unknown 
         */
        TrackInfo(int type, android.media.MediaFormat format) {
            mTrackType = type;
            mFormat = format;
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        /**
         * {@inheritDoc }
         */
        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mTrackType);
            dest.writeString(getLanguage());
            if (mTrackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE) {
                dest.writeString(mFormat.getString(android.media.MediaFormat.KEY_MIME));
                dest.writeInt(mFormat.getInteger(android.media.MediaFormat.KEY_IS_AUTOSELECT));
                dest.writeInt(mFormat.getInteger(android.media.MediaFormat.KEY_IS_DEFAULT));
                dest.writeInt(mFormat.getInteger(android.media.MediaFormat.KEY_IS_FORCED_SUBTITLE));
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder out = new java.lang.StringBuilder(128);
            out.append(getClass().getName());
            out.append('{');
            switch (mTrackType) {
                case android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_VIDEO :
                    out.append("VIDEO");
                    break;
                case android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO :
                    out.append("AUDIO");
                    break;
                case android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT :
                    out.append("TIMEDTEXT");
                    break;
                case android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE :
                    out.append("SUBTITLE");
                    break;
                default :
                    out.append("UNKNOWN");
                    break;
            }
            out.append(", " + mFormat.toString());
            out.append("}");
            return out.toString();
        }

        /**
         * Used to read a TrackInfo from a Parcel.
         */
        static final android.os.Parcelable.Creator<android.media.MediaPlayer.TrackInfo> CREATOR = new android.os.Parcelable.Creator<android.media.MediaPlayer.TrackInfo>() {
            @java.lang.Override
            public android.media.MediaPlayer.TrackInfo createFromParcel(android.os.Parcel in) {
                return new android.media.MediaPlayer.TrackInfo(in);
            }

            @java.lang.Override
            public android.media.MediaPlayer.TrackInfo[] newArray(int size) {
                return new android.media.MediaPlayer.TrackInfo[size];
            }
        };
    }

    // We would like domain specific classes with more informative names than the `first` and `second`
    // in generic Pair, but we would also like to avoid creating new/trivial classes. As a compromise
    // we document the meanings of `first` and `second` here:
    // 
    // Pair.first - inband track index; non-null iff representing an inband track.
    // Pair.second - a SubtitleTrack registered with mSubtitleController; non-null iff representing
    // an inband subtitle track or any out-of-band track (subtitle or timedtext).
    private java.util.Vector<android.util.Pair<java.lang.Integer, android.media.SubtitleTrack>> mIndexTrackPairs = new java.util.Vector<>();

    private java.util.BitSet mInbandTrackIndices = new java.util.BitSet();

    /**
     * Returns an array of track information.
     *
     * @return Array of track info. The total number of tracks is the array length.
    Must be called again if an external timed text source has been added after any of the
    addTimedTextSource methods are called.
     * @throws IllegalStateException
     * 		if it is called in an invalid state.
     */
    public android.media.MediaPlayer.TrackInfo[] getTrackInfo() throws java.lang.IllegalStateException {
        android.media.MediaPlayer.TrackInfo[] trackInfo = getInbandTrackInfo();
        // add out-of-band tracks
        synchronized(mIndexTrackPairs) {
            android.media.MediaPlayer.TrackInfo[] allTrackInfo = new android.media.MediaPlayer.TrackInfo[mIndexTrackPairs.size()];
            for (int i = 0; i < allTrackInfo.length; i++) {
                android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p = mIndexTrackPairs.get(i);
                if (p.first != null) {
                    // inband track
                    allTrackInfo[i] = trackInfo[p.first];
                } else {
                    android.media.SubtitleTrack track = p.second;
                    allTrackInfo[i] = new android.media.MediaPlayer.TrackInfo(track.getTrackType(), track.getFormat());
                }
            }
            return allTrackInfo;
        }
    }

    private android.media.MediaPlayer.TrackInfo[] getInbandTrackInfo() throws java.lang.IllegalStateException {
        android.os.Parcel request = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            request.writeInterfaceToken(android.media.MediaPlayer.IMEDIA_PLAYER);
            request.writeInt(android.media.MediaPlayer.INVOKE_ID_GET_TRACK_INFO);
            invoke(request, reply);
            android.media.MediaPlayer.TrackInfo[] trackInfo = reply.createTypedArray(android.media.MediaPlayer.TrackInfo.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    /* Do not change these values without updating their counterparts
    in include/media/stagefright/MediaDefs.h and media/libstagefright/MediaDefs.cpp!
     */
    /**
     * MIME type for SubRip (SRT) container. Used in addTimedTextSource APIs.
     */
    public static final java.lang.String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";

    /**
     * MIME type for WebVTT subtitle data.
     *
     * @unknown 
     */
    public static final java.lang.String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";

    /**
     * MIME type for CEA-608 closed caption data.
     *
     * @unknown 
     */
    public static final java.lang.String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";

    /**
     * MIME type for CEA-708 closed caption data.
     *
     * @unknown 
     */
    public static final java.lang.String MEDIA_MIMETYPE_TEXT_CEA_708 = "text/cea-708";

    /* A helper function to check if the mime type is supported by media framework. */
    private static boolean availableMimeTypeForExternalSource(java.lang.String mimeType) {
        if (android.media.MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP.equals(mimeType)) {
            return true;
        }
        return false;
    }

    private android.media.SubtitleController mSubtitleController;

    /**
     *
     *
     * @unknown 
     */
    public void setSubtitleAnchor(android.media.SubtitleController controller, android.media.SubtitleController.Anchor anchor) {
        // TODO: create SubtitleController in MediaPlayer
        mSubtitleController = controller;
        mSubtitleController.setAnchor(anchor);
    }

    /**
     * The private version of setSubtitleAnchor is used internally to set mSubtitleController if
     * necessary when clients don't provide their own SubtitleControllers using the public version
     * {@link #setSubtitleAnchor(SubtitleController, Anchor)} (e.g. {@link VideoView} provides one).
     */
    private synchronized void setSubtitleAnchor() {
        if (mSubtitleController == null) {
            final android.os.HandlerThread thread = new android.os.HandlerThread("SetSubtitleAnchorThread");
            thread.start();
            android.os.Handler handler = new android.os.Handler(thread.getLooper());
            handler.post(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    android.content.Context context = android.app.ActivityThread.currentApplication();
                    mSubtitleController = new android.media.SubtitleController(context, mTimeProvider, android.media.MediaPlayer.this);
                    mSubtitleController.setAnchor(new android.media.SubtitleController.Anchor() {
                        @java.lang.Override
                        public void setSubtitleWidget(android.media.SubtitleTrack.RenderingWidget subtitleWidget) {
                        }

                        @java.lang.Override
                        public android.os.Looper getSubtitleLooper() {
                            return android.os.Looper.getMainLooper();
                        }
                    });
                    thread.getLooper().quitSafely();
                }
            });
            try {
                thread.join();
            } catch (java.lang.InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
                android.util.Log.w(android.media.MediaPlayer.TAG, "failed to join SetSubtitleAnchorThread");
            }
        }
    }

    private int mSelectedSubtitleTrackIndex = -1;

    private java.util.Vector<java.io.InputStream> mOpenSubtitleSources;

    private android.media.MediaPlayer.OnSubtitleDataListener mSubtitleDataListener = new android.media.MediaPlayer.OnSubtitleDataListener() {
        @java.lang.Override
        public void onSubtitleData(android.media.MediaPlayer mp, android.media.SubtitleData data) {
            int index = data.getTrackIndex();
            synchronized(mIndexTrackPairs) {
                for (android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p : mIndexTrackPairs) {
                    if (((p.first != null) && (p.first == index)) && (p.second != null)) {
                        // inband subtitle track that owns data
                        android.media.SubtitleTrack track = p.second;
                        track.onData(data);
                    }
                }
            }
        }
    };

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onSubtitleTrackSelected(android.media.SubtitleTrack track) {
        if (mSelectedSubtitleTrackIndex >= 0) {
            try {
                selectOrDeselectInbandTrack(mSelectedSubtitleTrackIndex, false);
            } catch (java.lang.IllegalStateException e) {
            }
            mSelectedSubtitleTrackIndex = -1;
        }
        setOnSubtitleDataListener(null);
        if (track == null) {
            return;
        }
        synchronized(mIndexTrackPairs) {
            for (android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p : mIndexTrackPairs) {
                if ((p.first != null) && (p.second == track)) {
                    // inband subtitle track that is selected
                    mSelectedSubtitleTrackIndex = p.first;
                    break;
                }
            }
        }
        if (mSelectedSubtitleTrackIndex >= 0) {
            try {
                selectOrDeselectInbandTrack(mSelectedSubtitleTrackIndex, true);
            } catch (java.lang.IllegalStateException e) {
            }
            setOnSubtitleDataListener(mSubtitleDataListener);
        }
        // no need to select out-of-band tracks
    }

    /**
     *
     *
     * @unknown 
     */
    public void addSubtitleSource(java.io.InputStream is, android.media.MediaFormat format) throws java.lang.IllegalStateException {
        final java.io.InputStream fIs = is;
        final android.media.MediaFormat fFormat = format;
        if (is != null) {
            // Ensure all input streams are closed.  It is also a handy
            // way to implement timeouts in the future.
            synchronized(mOpenSubtitleSources) {
                mOpenSubtitleSources.add(is);
            }
        } else {
            android.util.Log.w(android.media.MediaPlayer.TAG, "addSubtitleSource called with null InputStream");
        }
        getMediaTimeProvider();
        // process each subtitle in its own thread
        final android.os.HandlerThread thread = new android.os.HandlerThread("SubtitleReadThread", android.os.Process.THREAD_PRIORITY_BACKGROUND + android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
        thread.start();
        android.os.Handler handler = new android.os.Handler(thread.getLooper());
        handler.post(new java.lang.Runnable() {
            private int addTrack() {
                if ((fIs == null) || (mSubtitleController == null)) {
                    return android.media.MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                }
                android.media.SubtitleTrack track = mSubtitleController.addTrack(fFormat);
                if (track == null) {
                    return android.media.MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                }
                // TODO: do the conversion in the subtitle track
                java.util.Scanner scanner = new java.util.Scanner(fIs, "UTF-8");
                java.lang.String contents = scanner.useDelimiter("\\A").next();
                synchronized(mOpenSubtitleSources) {
                    mOpenSubtitleSources.remove(fIs);
                }
                scanner.close();
                synchronized(mIndexTrackPairs) {
                    mIndexTrackPairs.add(android.util.Pair.<java.lang.Integer, android.media.SubtitleTrack>create(null, track));
                }
                android.os.Handler h = mTimeProvider.mEventHandler;
                int what = android.media.MediaPlayer.TimeProvider.NOTIFY;
                int arg1 = android.media.MediaPlayer.TimeProvider.NOTIFY_TRACK_DATA;
                android.util.Pair<android.media.SubtitleTrack, byte[]> trackData = android.util.Pair.create(track, contents.getBytes());
                android.os.Message m = h.obtainMessage(what, arg1, 0, trackData);
                h.sendMessage(m);
                return android.media.MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE;
            }

            public void run() {
                int res = addTrack();
                if (mEventHandler != null) {
                    android.os.Message m = mEventHandler.obtainMessage(android.media.MediaPlayer.MEDIA_INFO, res, 0, null);
                    mEventHandler.sendMessage(m);
                }
                thread.getLooper().quitSafely();
            }
        });
    }

    private void scanInternalSubtitleTracks() {
        if (mSubtitleController == null) {
            android.util.Log.d(android.media.MediaPlayer.TAG, "setSubtitleAnchor in MediaPlayer");
            setSubtitleAnchor();
        }
        populateInbandTracks();
        if (mSubtitleController != null) {
            mSubtitleController.selectDefaultTrack();
        }
    }

    private void populateInbandTracks() {
        android.media.MediaPlayer.TrackInfo[] tracks = getInbandTrackInfo();
        synchronized(mIndexTrackPairs) {
            for (int i = 0; i < tracks.length; i++) {
                if (mInbandTrackIndices.get(i)) {
                    continue;
                } else {
                    mInbandTrackIndices.set(i);
                }
                // newly appeared inband track
                if (tracks[i].getTrackType() == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE) {
                    android.media.SubtitleTrack track = mSubtitleController.addTrack(tracks[i].getFormat());
                    mIndexTrackPairs.add(android.util.Pair.create(i, track));
                } else {
                    mIndexTrackPairs.add(android.util.Pair.<java.lang.Integer, android.media.SubtitleTrack>create(i, null));
                }
            }
        }
    }

    /* TODO: Limit the total number of external timed text source to a reasonable number. */
    /**
     * Adds an external timed text source file.
     *
     * Currently supported format is SubRip with the file extension .srt, case insensitive.
     * Note that a single external timed text source may contain multiple tracks in it.
     * One can find the total number of available tracks using {@link #getTrackInfo()} to see what
     * additional tracks become available after this method call.
     *
     * @param path
     * 		The file path of external timed text source file.
     * @param mimeType
     * 		The mime type of the file. Must be one of the mime types listed above.
     * @throws IOException
     * 		if the file cannot be accessed or is corrupted.
     * @throws IllegalArgumentException
     * 		if the mimeType is not supported.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     */
    public void addTimedTextSource(java.lang.String path, java.lang.String mimeType) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        if (!android.media.MediaPlayer.availableMimeTypeForExternalSource(mimeType)) {
            final java.lang.String msg = "Illegal mimeType for timed text source: " + mimeType;
            throw new java.lang.IllegalArgumentException(msg);
        }
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            java.io.FileInputStream is = new java.io.FileInputStream(file);
            java.io.FileDescriptor fd = is.getFD();
            addTimedTextSource(fd, mimeType);
            is.close();
        } else {
            // We do not support the case where the path is not a file.
            throw new java.io.IOException(path);
        }
    }

    /**
     * Adds an external timed text source file (Uri).
     *
     * Currently supported format is SubRip with the file extension .srt, case insensitive.
     * Note that a single external timed text source may contain multiple tracks in it.
     * One can find the total number of available tracks using {@link #getTrackInfo()} to see what
     * additional tracks become available after this method call.
     *
     * @param context
     * 		the Context to use when resolving the Uri
     * @param uri
     * 		the Content URI of the data you want to play
     * @param mimeType
     * 		The mime type of the file. Must be one of the mime types listed above.
     * @throws IOException
     * 		if the file cannot be accessed or is corrupted.
     * @throws IllegalArgumentException
     * 		if the mimeType is not supported.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     */
    public void addTimedTextSource(android.content.Context context, android.net.Uri uri, java.lang.String mimeType) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        java.lang.String scheme = uri.getScheme();
        if ((scheme == null) || scheme.equals("file")) {
            addTimedTextSource(uri.getPath(), mimeType);
            return;
        }
        android.content.res.AssetFileDescriptor fd = null;
        try {
            android.content.ContentResolver resolver = context.getContentResolver();
            fd = resolver.openAssetFileDescriptor(uri, "r");
            if (fd == null) {
                return;
            }
            addTimedTextSource(fd.getFileDescriptor(), mimeType);
            return;
        } catch (java.lang.SecurityException ex) {
        } catch (java.io.IOException ex) {
        } finally {
            if (fd != null) {
                fd.close();
            }
        }
    }

    /**
     * Adds an external timed text source file (FileDescriptor).
     *
     * It is the caller's responsibility to close the file descriptor.
     * It is safe to do so as soon as this call returns.
     *
     * Currently supported format is SubRip. Note that a single external timed text source may
     * contain multiple tracks in it. One can find the total number of available tracks
     * using {@link #getTrackInfo()} to see what additional tracks become available
     * after this method call.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to play
     * @param mimeType
     * 		The mime type of the file. Must be one of the mime types listed above.
     * @throws IllegalArgumentException
     * 		if the mimeType is not supported.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     */
    public void addTimedTextSource(java.io.FileDescriptor fd, java.lang.String mimeType) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        // intentionally less than LONG_MAX
        addTimedTextSource(fd, 0, 0x7ffffffffffffffL, mimeType);
    }

    /**
     * Adds an external timed text file (FileDescriptor).
     *
     * It is the caller's responsibility to close the file descriptor.
     * It is safe to do so as soon as this call returns.
     *
     * Currently supported format is SubRip. Note that a single external timed text source may
     * contain multiple tracks in it. One can find the total number of available tracks
     * using {@link #getTrackInfo()} to see what additional tracks become available
     * after this method call.
     *
     * @param fd
     * 		the FileDescriptor for the file you want to play
     * @param offset
     * 		the offset into the file where the data to be played starts, in bytes
     * @param length
     * 		the length in bytes of the data to be played
     * @param mime
     * 		The mime type of the file. Must be one of the mime types listed above.
     * @throws IllegalArgumentException
     * 		if the mimeType is not supported.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     */
    public void addTimedTextSource(java.io.FileDescriptor fd, long offset, long length, java.lang.String mime) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        if (!android.media.MediaPlayer.availableMimeTypeForExternalSource(mime)) {
            throw new java.lang.IllegalArgumentException("Illegal mimeType for timed text source: " + mime);
        }
        java.io.FileDescriptor fd2;
        try {
            fd2 = Libcore.os.dup(fd);
        } catch (android.system.ErrnoException ex) {
            android.util.Log.e(android.media.MediaPlayer.TAG, ex.getMessage(), ex);
            throw new java.lang.RuntimeException(ex);
        }
        final android.media.MediaFormat fFormat = new android.media.MediaFormat();
        fFormat.setString(android.media.MediaFormat.KEY_MIME, mime);
        fFormat.setInteger(android.media.MediaFormat.KEY_IS_TIMED_TEXT, 1);
        // A MediaPlayer created by a VideoView should already have its mSubtitleController set.
        if (mSubtitleController == null) {
            setSubtitleAnchor();
        }
        if (!mSubtitleController.hasRendererFor(fFormat)) {
            // test and add not atomic
            android.content.Context context = android.app.ActivityThread.currentApplication();
            mSubtitleController.registerRenderer(new android.media.SRTRenderer(context, mEventHandler));
        }
        final android.media.SubtitleTrack track = mSubtitleController.addTrack(fFormat);
        synchronized(mIndexTrackPairs) {
            mIndexTrackPairs.add(android.util.Pair.<java.lang.Integer, android.media.SubtitleTrack>create(null, track));
        }
        getMediaTimeProvider();
        final java.io.FileDescriptor fd3 = fd2;
        final long offset2 = offset;
        final long length2 = length;
        final android.os.HandlerThread thread = new android.os.HandlerThread("TimedTextReadThread", android.os.Process.THREAD_PRIORITY_BACKGROUND + android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
        thread.start();
        android.os.Handler handler = new android.os.Handler(thread.getLooper());
        handler.post(new java.lang.Runnable() {
            private int addTrack() {
                java.io.InputStream is = null;
                final java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
                try {
                    Libcore.os.lseek(fd3, offset2, android.system.OsConstants.SEEK_SET);
                    byte[] buffer = new byte[4096];
                    for (long total = 0; total < length2;) {
                        int bytesToRead = ((int) (java.lang.Math.min(buffer.length, length2 - total)));
                        int bytes = libcore.io.IoBridge.read(fd3, buffer, 0, bytesToRead);
                        if (bytes < 0) {
                            break;
                        } else {
                            bos.write(buffer, 0, bytes);
                            total += bytes;
                        }
                    }
                    android.os.Handler h = mTimeProvider.mEventHandler;
                    int what = android.media.MediaPlayer.TimeProvider.NOTIFY;
                    int arg1 = android.media.MediaPlayer.TimeProvider.NOTIFY_TRACK_DATA;
                    android.util.Pair<android.media.SubtitleTrack, byte[]> trackData = android.util.Pair.create(track, bos.toByteArray());
                    android.os.Message m = h.obtainMessage(what, arg1, 0, trackData);
                    h.sendMessage(m);
                    return android.media.MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE;
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.media.MediaPlayer.TAG, e.getMessage(), e);
                    return android.media.MediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR;
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (java.io.IOException e) {
                            android.util.Log.e(android.media.MediaPlayer.TAG, e.getMessage(), e);
                        }
                    }
                }
            }

            public void run() {
                int res = addTrack();
                if (mEventHandler != null) {
                    android.os.Message m = mEventHandler.obtainMessage(android.media.MediaPlayer.MEDIA_INFO, res, 0, null);
                    mEventHandler.sendMessage(m);
                }
                thread.getLooper().quitSafely();
            }
        });
    }

    /**
     * Returns the index of the audio, video, or subtitle track currently selected for playback,
     * The return value is an index into the array returned by {@link #getTrackInfo()}, and can
     * be used in calls to {@link #selectTrack(int)} or {@link #deselectTrack(int)}.
     *
     * @param trackType
     * 		should be one of {@link TrackInfo#MEDIA_TRACK_TYPE_VIDEO},
     * 		{@link TrackInfo#MEDIA_TRACK_TYPE_AUDIO}, or
     * 		{@link TrackInfo#MEDIA_TRACK_TYPE_SUBTITLE}
     * @return index of the audio, video, or subtitle track currently selected for playback;
    a negative integer is returned when there is no selected track for {@code trackType} or
    when {@code trackType} is not one of audio, video, or subtitle.
     * @throws IllegalStateException
     * 		if called after {@link #release()}
     * @see #getTrackInfo()
     * @see #selectTrack(int)
     * @see #deselectTrack(int)
     */
    public int getSelectedTrack(int trackType) throws java.lang.IllegalStateException {
        if ((mSubtitleController != null) && ((trackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_SUBTITLE) || (trackType == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT))) {
            android.media.SubtitleTrack subtitleTrack = mSubtitleController.getSelectedTrack();
            if (subtitleTrack != null) {
                synchronized(mIndexTrackPairs) {
                    for (int i = 0; i < mIndexTrackPairs.size(); i++) {
                        android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p = mIndexTrackPairs.get(i);
                        if ((p.second == subtitleTrack) && (subtitleTrack.getTrackType() == trackType)) {
                            return i;
                        }
                    }
                }
            }
        }
        android.os.Parcel request = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            request.writeInterfaceToken(android.media.MediaPlayer.IMEDIA_PLAYER);
            request.writeInt(android.media.MediaPlayer.INVOKE_ID_GET_SELECTED_TRACK);
            request.writeInt(trackType);
            invoke(request, reply);
            int inbandTrackIndex = reply.readInt();
            synchronized(mIndexTrackPairs) {
                for (int i = 0; i < mIndexTrackPairs.size(); i++) {
                    android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p = mIndexTrackPairs.get(i);
                    if ((p.first != null) && (p.first == inbandTrackIndex)) {
                        return i;
                    }
                }
            }
            return -1;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    /**
     * Selects a track.
     * <p>
     * If a MediaPlayer is in invalid state, it throws an IllegalStateException exception.
     * If a MediaPlayer is in <em>Started</em> state, the selected track is presented immediately.
     * If a MediaPlayer is not in Started state, it just marks the track to be played.
     * </p>
     * <p>
     * In any valid state, if it is called multiple times on the same type of track (ie. Video,
     * Audio, Timed Text), the most recent one will be chosen.
     * </p>
     * <p>
     * The first audio and video tracks are selected by default if available, even though
     * this method is not called. However, no timed text track will be selected until
     * this function is called.
     * </p>
     * <p>
     * Currently, only timed text tracks or audio tracks can be selected via this method.
     * In addition, the support for selecting an audio track at runtime is pretty limited
     * in that an audio track can only be selected in the <em>Prepared</em> state.
     * </p>
     *
     * @param index
     * 		the index of the track to be selected. The valid range of the index
     * 		is 0..total number of track - 1. The total number of tracks as well as the type of
     * 		each individual track can be found by calling {@link #getTrackInfo()} method.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     * @see android.media.MediaPlayer#getTrackInfo
     */
    public void selectTrack(int index) throws java.lang.IllegalStateException {
        /* select */
        selectOrDeselectTrack(index, true);
    }

    /**
     * Deselect a track.
     * <p>
     * Currently, the track must be a timed text track and no audio or video tracks can be
     * deselected. If the timed text track identified by index has not been
     * selected before, it throws an exception.
     * </p>
     *
     * @param index
     * 		the index of the track to be deselected. The valid range of the index
     * 		is 0..total number of tracks - 1. The total number of tracks as well as the type of
     * 		each individual track can be found by calling {@link #getTrackInfo()} method.
     * @throws IllegalStateException
     * 		if called in an invalid state.
     * @see android.media.MediaPlayer#getTrackInfo
     */
    public void deselectTrack(int index) throws java.lang.IllegalStateException {
        /* select */
        selectOrDeselectTrack(index, false);
    }

    private void selectOrDeselectTrack(int index, boolean select) throws java.lang.IllegalStateException {
        // handle subtitle track through subtitle controller
        populateInbandTracks();
        android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p = null;
        try {
            p = mIndexTrackPairs.get(index);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            // ignore bad index
            return;
        }
        android.media.SubtitleTrack track = p.second;
        if (track == null) {
            // inband (de)select
            selectOrDeselectInbandTrack(p.first, select);
            return;
        }
        if (mSubtitleController == null) {
            return;
        }
        if (!select) {
            // out-of-band deselect
            if (mSubtitleController.getSelectedTrack() == track) {
                mSubtitleController.selectTrack(null);
            } else {
                android.util.Log.w(android.media.MediaPlayer.TAG, "trying to deselect track that was not selected");
            }
            return;
        }
        // out-of-band select
        if (track.getTrackType() == android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT) {
            int ttIndex = getSelectedTrack(android.media.MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT);
            synchronized(mIndexTrackPairs) {
                if ((ttIndex >= 0) && (ttIndex < mIndexTrackPairs.size())) {
                    android.util.Pair<java.lang.Integer, android.media.SubtitleTrack> p2 = mIndexTrackPairs.get(ttIndex);
                    if ((p2.first != null) && (p2.second == null)) {
                        // deselect inband counterpart
                        selectOrDeselectInbandTrack(p2.first, false);
                    }
                }
            }
        }
        mSubtitleController.selectTrack(track);
    }

    private void selectOrDeselectInbandTrack(int index, boolean select) throws java.lang.IllegalStateException {
        android.os.Parcel request = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        try {
            request.writeInterfaceToken(android.media.MediaPlayer.IMEDIA_PLAYER);
            request.writeInt(select ? android.media.MediaPlayer.INVOKE_ID_SELECT_TRACK : android.media.MediaPlayer.INVOKE_ID_DESELECT_TRACK);
            request.writeInt(index);
            invoke(request, reply);
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    /**
     *
     *
     * @param reply
     * 		Parcel with audio/video duration info for battery
     * 		tracking usage
     * @return The status code.
    {@hide }
     */
    public static native int native_pullBatteryData(android.os.Parcel reply);

    /**
     * Sets the target UDP re-transmit endpoint for the low level player.
     * Generally, the address portion of the endpoint is an IP multicast
     * address, although a unicast address would be equally valid.  When a valid
     * retransmit endpoint has been set, the media player will not decode and
     * render the media presentation locally.  Instead, the player will attempt
     * to re-multiplex its media data using the Android@Home RTP profile and
     * re-transmit to the target endpoint.  Receiver devices (which may be
     * either the same as the transmitting device or different devices) may
     * instantiate, prepare, and start a receiver player using a setDataSource
     * URL of the form...
     *
     * aahRX://&lt;multicastIP&gt;:&lt;port&gt;
     *
     * to receive, decode and render the re-transmitted content.
     *
     * setRetransmitEndpoint may only be called before setDataSource has been
     * called; while the player is in the Idle state.
     *
     * @param endpoint
     * 		the address and UDP port of the re-transmission target or
     * 		null if no re-transmission is to be performed.
     * @throws IllegalStateException
     * 		if it is called in an invalid state
     * @throws IllegalArgumentException
     * 		if the retransmit endpoint is supplied,
     * 		but invalid.
     * 		
     * 		{@hide } pending API council
     */
    public void setRetransmitEndpoint(java.net.InetSocketAddress endpoint) throws java.lang.IllegalArgumentException, java.lang.IllegalStateException {
        java.lang.String addrString = null;
        int port = 0;
        if (null != endpoint) {
            addrString = endpoint.getAddress().getHostAddress();
            port = endpoint.getPort();
        }
        int ret = native_setRetransmitEndpoint(addrString, port);
        if (ret != 0) {
            throw new java.lang.IllegalArgumentException("Illegal re-transmit endpoint; native ret " + ret);
        }
    }

    private final native int native_setRetransmitEndpoint(java.lang.String addrString, int port);

    @java.lang.Override
    protected void finalize() {
        baseRelease();
        native_finalize();
    }

    /* Do not change these values without updating their counterparts
    in include/media/mediaplayer.h!
     */
    private static final int MEDIA_NOP = 0;// interface test message


    private static final int MEDIA_PREPARED = 1;

    private static final int MEDIA_PLAYBACK_COMPLETE = 2;

    private static final int MEDIA_BUFFERING_UPDATE = 3;

    private static final int MEDIA_SEEK_COMPLETE = 4;

    private static final int MEDIA_SET_VIDEO_SIZE = 5;

    private static final int MEDIA_STARTED = 6;

    private static final int MEDIA_PAUSED = 7;

    private static final int MEDIA_STOPPED = 8;

    private static final int MEDIA_SKIPPED = 9;

    private static final int MEDIA_TIMED_TEXT = 99;

    private static final int MEDIA_ERROR = 100;

    private static final int MEDIA_INFO = 200;

    private static final int MEDIA_SUBTITLE_DATA = 201;

    private static final int MEDIA_META_DATA = 202;

    private android.media.MediaPlayer.TimeProvider mTimeProvider;

    /**
     *
     *
     * @unknown 
     */
    public android.media.MediaTimeProvider getMediaTimeProvider() {
        if (mTimeProvider == null) {
            mTimeProvider = new android.media.MediaPlayer.TimeProvider(this);
        }
        return mTimeProvider;
    }

    private class EventHandler extends android.os.Handler {
        private android.media.MediaPlayer mMediaPlayer;

        public EventHandler(android.media.MediaPlayer mp, android.os.Looper looper) {
            super(looper);
            mMediaPlayer = mp;
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            if (mMediaPlayer.mNativeContext == 0) {
                android.util.Log.w(android.media.MediaPlayer.TAG, "mediaplayer went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case android.media.MediaPlayer.MEDIA_PREPARED :
                    try {
                        scanInternalSubtitleTracks();
                    } catch (java.lang.RuntimeException e) {
                        // send error message instead of crashing;
                        // send error message instead of inlining a call to onError
                        // to avoid code duplication.
                        android.os.Message msg2 = obtainMessage(android.media.MediaPlayer.MEDIA_ERROR, android.media.MediaPlayer.MEDIA_ERROR_UNKNOWN, android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED, null);
                        sendMessage(msg2);
                    }
                    android.media.MediaPlayer.OnPreparedListener onPreparedListener = mOnPreparedListener;
                    if (onPreparedListener != null)
                        onPreparedListener.onPrepared(mMediaPlayer);

                    return;
                case android.media.MediaPlayer.MEDIA_PLAYBACK_COMPLETE :
                    {
                        android.media.MediaPlayer.OnCompletionListener onCompletionListener = mOnCompletionListener;
                        if (onCompletionListener != null)
                            onCompletionListener.onCompletion(mMediaPlayer);

                    }
                    stayAwake(false);
                    return;
                case android.media.MediaPlayer.MEDIA_STOPPED :
                    {
                        android.media.MediaPlayer.TimeProvider timeProvider = mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onStopped();
                        }
                    }
                    break;
                case android.media.MediaPlayer.MEDIA_STARTED :
                case android.media.MediaPlayer.MEDIA_PAUSED :
                    {
                        android.media.MediaPlayer.TimeProvider timeProvider = mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onPaused(msg.what == android.media.MediaPlayer.MEDIA_PAUSED);
                        }
                    }
                    break;
                case android.media.MediaPlayer.MEDIA_BUFFERING_UPDATE :
                    android.media.MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = mOnBufferingUpdateListener;
                    if (onBufferingUpdateListener != null)
                        onBufferingUpdateListener.onBufferingUpdate(mMediaPlayer, msg.arg1);

                    return;
                case android.media.MediaPlayer.MEDIA_SEEK_COMPLETE :
                    android.media.MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = mOnSeekCompleteListener;
                    if (onSeekCompleteListener != null) {
                        onSeekCompleteListener.onSeekComplete(mMediaPlayer);
                    }
                    // fall through
                case android.media.MediaPlayer.MEDIA_SKIPPED :
                    {
                        android.media.MediaPlayer.TimeProvider timeProvider = mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onSeekComplete(mMediaPlayer);
                        }
                    }
                    return;
                case android.media.MediaPlayer.MEDIA_SET_VIDEO_SIZE :
                    android.media.MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = mOnVideoSizeChangedListener;
                    if (onVideoSizeChangedListener != null) {
                        onVideoSizeChangedListener.onVideoSizeChanged(mMediaPlayer, msg.arg1, msg.arg2);
                    }
                    return;
                case android.media.MediaPlayer.MEDIA_ERROR :
                    android.util.Log.e(android.media.MediaPlayer.TAG, ((("Error (" + msg.arg1) + ",") + msg.arg2) + ")");
                    boolean error_was_handled = false;
                    android.media.MediaPlayer.OnErrorListener onErrorListener = mOnErrorListener;
                    if (onErrorListener != null) {
                        error_was_handled = onErrorListener.onError(mMediaPlayer, msg.arg1, msg.arg2);
                    }
                    {
                        android.media.MediaPlayer.OnCompletionListener onCompletionListener = mOnCompletionListener;
                        if ((onCompletionListener != null) && (!error_was_handled)) {
                            onCompletionListener.onCompletion(mMediaPlayer);
                        }
                    }
                    stayAwake(false);
                    return;
                case android.media.MediaPlayer.MEDIA_INFO :
                    switch (msg.arg1) {
                        case android.media.MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING :
                            android.util.Log.i(android.media.MediaPlayer.TAG, ((("Info (" + msg.arg1) + ",") + msg.arg2) + ")");
                            break;
                        case android.media.MediaPlayer.MEDIA_INFO_METADATA_UPDATE :
                            try {
                                scanInternalSubtitleTracks();
                            } catch (java.lang.RuntimeException e) {
                                android.os.Message msg2 = obtainMessage(android.media.MediaPlayer.MEDIA_ERROR, android.media.MediaPlayer.MEDIA_ERROR_UNKNOWN, android.media.MediaPlayer.MEDIA_ERROR_UNSUPPORTED, null);
                                sendMessage(msg2);
                            }
                            // fall through
                        case android.media.MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE :
                            msg.arg1 = android.media.MediaPlayer.MEDIA_INFO_METADATA_UPDATE;
                            // update default track selection
                            if (mSubtitleController != null) {
                                mSubtitleController.selectDefaultTrack();
                            }
                            break;
                        case android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START :
                        case android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END :
                            android.media.MediaPlayer.TimeProvider timeProvider = mTimeProvider;
                            if (timeProvider != null) {
                                timeProvider.onBuffering(msg.arg1 == android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START);
                            }
                            break;
                    }
                    android.media.MediaPlayer.OnInfoListener onInfoListener = mOnInfoListener;
                    if (onInfoListener != null) {
                        onInfoListener.onInfo(mMediaPlayer, msg.arg1, msg.arg2);
                    }
                    // No real default action so far.
                    return;
                case android.media.MediaPlayer.MEDIA_TIMED_TEXT :
                    android.media.MediaPlayer.OnTimedTextListener onTimedTextListener = mOnTimedTextListener;
                    if (onTimedTextListener == null)
                        return;

                    if (msg.obj == null) {
                        onTimedTextListener.onTimedText(mMediaPlayer, null);
                    } else {
                        if (msg.obj instanceof android.os.Parcel) {
                            android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                            android.media.TimedText text = new android.media.TimedText(parcel);
                            parcel.recycle();
                            onTimedTextListener.onTimedText(mMediaPlayer, text);
                        }
                    }
                    return;
                case android.media.MediaPlayer.MEDIA_SUBTITLE_DATA :
                    android.media.MediaPlayer.OnSubtitleDataListener onSubtitleDataListener = mOnSubtitleDataListener;
                    if (onSubtitleDataListener == null) {
                        return;
                    }
                    if (msg.obj instanceof android.os.Parcel) {
                        android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                        android.media.SubtitleData data = new android.media.SubtitleData(parcel);
                        parcel.recycle();
                        onSubtitleDataListener.onSubtitleData(mMediaPlayer, data);
                    }
                    return;
                case android.media.MediaPlayer.MEDIA_META_DATA :
                    android.media.MediaPlayer.OnTimedMetaDataAvailableListener onTimedMetaDataAvailableListener = mOnTimedMetaDataAvailableListener;
                    if (onTimedMetaDataAvailableListener == null) {
                        return;
                    }
                    if (msg.obj instanceof android.os.Parcel) {
                        android.os.Parcel parcel = ((android.os.Parcel) (msg.obj));
                        android.media.TimedMetaData data = android.media.TimedMetaData.createTimedMetaDataFromParcel(parcel);
                        parcel.recycle();
                        onTimedMetaDataAvailableListener.onTimedMetaDataAvailable(mMediaPlayer, data);
                    }
                    return;
                case android.media.MediaPlayer.MEDIA_NOP :
                    // interface test message - ignore
                    break;
                default :
                    android.util.Log.e(android.media.MediaPlayer.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    /* Called from native code when an interesting event happens.  This method
    just uses the EventHandler system to post the event back to the main app thread.
    We use a weak reference to the original MediaPlayer object so that the native
    code is safe from the object disappearing from underneath it.  (This is
    the cookie passed to native_setup().)
     */
    private static void postEventFromNative(java.lang.Object mediaplayer_ref, int what, int arg1, int arg2, java.lang.Object obj) {
        android.media.MediaPlayer mp = ((android.media.MediaPlayer) (((java.lang.ref.WeakReference) (mediaplayer_ref)).get()));
        if (mp == null) {
            return;
        }
        if ((what == android.media.MediaPlayer.MEDIA_INFO) && (arg1 == android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT)) {
            // this acquires the wakelock if needed, and sets the client side state
            mp.start();
        }
        if (mp.mEventHandler != null) {
            android.os.Message m = mp.mEventHandler.obtainMessage(what, arg1, arg2, obj);
            mp.mEventHandler.sendMessage(m);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the media
     * source is ready for playback.
     */
    public interface OnPreparedListener {
        /**
         * Called when the media file is ready for playback.
         *
         * @param mp
         * 		the MediaPlayer that is ready for playback
         */
        void onPrepared(android.media.MediaPlayer mp);
    }

    /**
     * Register a callback to be invoked when the media source is ready
     * for playback.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnPreparedListener(android.media.MediaPlayer.OnPreparedListener listener) {
        mOnPreparedListener = listener;
    }

    private android.media.MediaPlayer.OnPreparedListener mOnPreparedListener;

    /**
     * Interface definition for a callback to be invoked when playback of
     * a media source has completed.
     */
    public interface OnCompletionListener {
        /**
         * Called when the end of a media source is reached during playback.
         *
         * @param mp
         * 		the MediaPlayer that reached the end of the file
         */
        void onCompletion(android.media.MediaPlayer mp);
    }

    /**
     * Register a callback to be invoked when the end of a media source
     * has been reached during playback.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnCompletionListener(android.media.MediaPlayer.OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    private android.media.MediaPlayer.OnCompletionListener mOnCompletionListener;

    /**
     * Interface definition of a callback to be invoked indicating buffering
     * status of a media resource being streamed over the network.
     */
    public interface OnBufferingUpdateListener {
        /**
         * Called to update status in buffering a media stream received through
         * progressive HTTP download. The received buffering percentage
         * indicates how much of the content has been buffered or played.
         * For example a buffering update of 80 percent when half the content
         * has already been played indicates that the next 30 percent of the
         * content to play has been buffered.
         *
         * @param mp
         * 		the MediaPlayer the update pertains to
         * @param percent
         * 		the percentage (0-100) of the content
         * 		that has been buffered or played thus far
         */
        void onBufferingUpdate(android.media.MediaPlayer mp, int percent);
    }

    /**
     * Register a callback to be invoked when the status of a network
     * stream's buffer has changed.
     *
     * @param listener
     * 		the callback that will be run.
     */
    public void setOnBufferingUpdateListener(android.media.MediaPlayer.OnBufferingUpdateListener listener) {
        mOnBufferingUpdateListener = listener;
    }

    private android.media.MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;

    /**
     * Interface definition of a callback to be invoked indicating
     * the completion of a seek operation.
     */
    public interface OnSeekCompleteListener {
        /**
         * Called to indicate the completion of a seek operation.
         *
         * @param mp
         * 		the MediaPlayer that issued the seek operation
         */
        public void onSeekComplete(android.media.MediaPlayer mp);
    }

    /**
     * Register a callback to be invoked when a seek operation has been
     * completed.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnSeekCompleteListener(android.media.MediaPlayer.OnSeekCompleteListener listener) {
        mOnSeekCompleteListener = listener;
    }

    private android.media.MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    /**
     * Interface definition of a callback to be invoked when the
     * video size is first known or updated
     */
    public interface OnVideoSizeChangedListener {
        /**
         * Called to indicate the video size
         *
         * The video size (width and height) could be 0 if there was no video,
         * no display surface was set, or the value was not determined yet.
         *
         * @param mp
         * 		the MediaPlayer associated with this callback
         * @param width
         * 		the width of the video
         * @param height
         * 		the height of the video
         */
        public void onVideoSizeChanged(android.media.MediaPlayer mp, int width, int height);
    }

    /**
     * Register a callback to be invoked when the video size is
     * known or updated.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnVideoSizeChangedListener(android.media.MediaPlayer.OnVideoSizeChangedListener listener) {
        mOnVideoSizeChangedListener = listener;
    }

    private android.media.MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    /**
     * Interface definition of a callback to be invoked when a
     * timed text is available for display.
     */
    public interface OnTimedTextListener {
        /**
         * Called to indicate an avaliable timed text
         *
         * @param mp
         * 		the MediaPlayer associated with this callback
         * @param text
         * 		the timed text sample which contains the text
         * 		needed to be displayed and the display format.
         */
        public void onTimedText(android.media.MediaPlayer mp, android.media.TimedText text);
    }

    /**
     * Register a callback to be invoked when a timed text is available
     * for display.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnTimedTextListener(android.media.MediaPlayer.OnTimedTextListener listener) {
        mOnTimedTextListener = listener;
    }

    private android.media.MediaPlayer.OnTimedTextListener mOnTimedTextListener;

    /**
     * Interface definition of a callback to be invoked when a
     * track has data available.
     *
     * @unknown 
     */
    public interface OnSubtitleDataListener {
        public void onSubtitleData(android.media.MediaPlayer mp, android.media.SubtitleData data);
    }

    /**
     * Register a callback to be invoked when a track has data available.
     *
     * @param listener
     * 		the callback that will be run
     * @unknown 
     */
    public void setOnSubtitleDataListener(android.media.MediaPlayer.OnSubtitleDataListener listener) {
        mOnSubtitleDataListener = listener;
    }

    private android.media.MediaPlayer.OnSubtitleDataListener mOnSubtitleDataListener;

    /**
     * Interface definition of a callback to be invoked when a
     * track has timed metadata available.
     *
     * @see MediaPlayer#setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener)
     */
    public interface OnTimedMetaDataAvailableListener {
        /**
         * Called to indicate avaliable timed metadata
         * <p>
         * This method will be called as timed metadata is extracted from the media,
         * in the same order as it occurs in the media. The timing of this event is
         * not controlled by the associated timestamp.
         *
         * @param mp
         * 		the MediaPlayer associated with this callback
         * @param data
         * 		the timed metadata sample associated with this event
         */
        public void onTimedMetaDataAvailable(android.media.MediaPlayer mp, android.media.TimedMetaData data);
    }

    /**
     * Register a callback to be invoked when a selected track has timed metadata available.
     * <p>
     * Currently only HTTP live streaming data URI's embedded with timed ID3 tags generates
     * {@link TimedMetaData}.
     *
     * @see MediaPlayer#selectTrack(int)
     * @see MediaPlayer.OnTimedMetaDataAvailableListener
     * @see TimedMetaData
     * @param listener
     * 		the callback that will be run
     */
    public void setOnTimedMetaDataAvailableListener(android.media.MediaPlayer.OnTimedMetaDataAvailableListener listener) {
        mOnTimedMetaDataAvailableListener = listener;
    }

    private android.media.MediaPlayer.OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;

    /* Do not change these values without updating their counterparts
    in include/media/mediaplayer.h!
     */
    /**
     * Unspecified media player error.
     *
     * @see android.media.MediaPlayer.OnErrorListener
     */
    public static final int MEDIA_ERROR_UNKNOWN = 1;

    /**
     * Media server died. In this case, the application must release the
     * MediaPlayer object and instantiate a new one.
     *
     * @see android.media.MediaPlayer.OnErrorListener
     */
    public static final int MEDIA_ERROR_SERVER_DIED = 100;

    /**
     * The video is streamed and its container is not valid for progressive
     * playback i.e the video's index (e.g moov atom) is not at the start of the
     * file.
     *
     * @see android.media.MediaPlayer.OnErrorListener
     */
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;

    /**
     * File or network related operation errors.
     */
    public static final int MEDIA_ERROR_IO = -1004;

    /**
     * Bitstream is not conforming to the related coding standard or file spec.
     */
    public static final int MEDIA_ERROR_MALFORMED = -1007;

    /**
     * Bitstream is conforming to the related coding standard or file spec, but
     * the media framework does not support the feature.
     */
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;

    /**
     * Some operation takes too long to complete, usually more than 3-5 seconds.
     */
    public static final int MEDIA_ERROR_TIMED_OUT = -110;

    /**
     * Unspecified low-level system error. This value originated from UNKNOWN_ERROR in
     * system/core/include/utils/Errors.h
     *
     * @see android.media.MediaPlayer.OnErrorListener
     * @unknown 
     */
    public static final int MEDIA_ERROR_SYSTEM = -2147483648;

    /**
     * Interface definition of a callback to be invoked when there
     * has been an error during an asynchronous operation (other errors
     * will throw exceptions at method call time).
     */
    public interface OnErrorListener {
        /**
         * Called to indicate an error.
         *
         * @param mp
         * 		the MediaPlayer the error pertains to
         * @param what
         * 		the type of error that has occurred:
         * 		<ul>
         * 		<li>{@link #MEDIA_ERROR_UNKNOWN}
         * 		<li>{@link #MEDIA_ERROR_SERVER_DIED}
         * 		</ul>
         * @param extra
         * 		an extra code, specific to the error. Typically
         * 		implementation dependent.
         * 		<ul>
         * 		<li>{@link #MEDIA_ERROR_IO}
         * 		<li>{@link #MEDIA_ERROR_MALFORMED}
         * 		<li>{@link #MEDIA_ERROR_UNSUPPORTED}
         * 		<li>{@link #MEDIA_ERROR_TIMED_OUT}
         * 		<li><code>MEDIA_ERROR_SYSTEM (-2147483648)</code> - low-level system error.
         * 		</ul>
         * @return True if the method handled the error, false if it didn't.
        Returning false, or not having an OnErrorListener at all, will
        cause the OnCompletionListener to be called.
         */
        boolean onError(android.media.MediaPlayer mp, int what, int extra);
    }

    /**
     * Register a callback to be invoked when an error has happened
     * during an asynchronous operation.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnErrorListener(android.media.MediaPlayer.OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    private android.media.MediaPlayer.OnErrorListener mOnErrorListener;

    /* Do not change these values without updating their counterparts
    in include/media/mediaplayer.h!
     */
    /**
     * Unspecified media player info.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_UNKNOWN = 1;

    /**
     * The player was started because it was used as the next player for another
     * player, which just completed playback.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     * @unknown 
     */
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;

    /**
     * The player just pushed the very first video frame for rendering.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;

    /**
     * The video is too complex for the decoder: it can't decode frames fast
     *  enough. Possibly only the audio plays fine at this stage.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;

    /**
     * MediaPlayer is temporarily pausing playback internally in order to
     * buffer more data.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_BUFFERING_START = 701;

    /**
     * MediaPlayer is resuming playback after filling buffers.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_BUFFERING_END = 702;

    /**
     * Estimated network bandwidth information (kbps) is available; currently this event fires
     * simultaneously as {@link #MEDIA_INFO_BUFFERING_START} and {@link #MEDIA_INFO_BUFFERING_END}
     * when playing network files.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     * @unknown 
     */
    public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;

    /**
     * Bad interleaving means that a media has been improperly interleaved or
     * not interleaved at all, e.g has all the video samples first then all the
     * audio ones. Video is playing but a lot of disk seeks may be happening.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;

    /**
     * The media cannot be seeked (e.g live stream)
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;

    /**
     * A new set of metadata is available.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;

    /**
     * A new set of external-only metadata is available.  Used by
     *  JAVA framework to avoid triggering track scanning.
     *
     * @unknown 
     */
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;

    /**
     * Failed to handle timed text track properly.
     *
     * @see android.media.MediaPlayer.OnInfoListener

    {@hide }
     */
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;

    /**
     * Subtitle track was not supported by the media framework.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;

    /**
     * Reading the subtitle track takes too long.
     *
     * @see android.media.MediaPlayer.OnInfoListener
     */
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;

    /**
     * Interface definition of a callback to be invoked to communicate some
     * info and/or warning about the media or its playback.
     */
    public interface OnInfoListener {
        /**
         * Called to indicate an info or a warning.
         *
         * @param mp
         * 		the MediaPlayer the info pertains to.
         * @param what
         * 		the type of info or warning.
         * 		<ul>
         * 		<li>{@link #MEDIA_INFO_UNKNOWN}
         * 		<li>{@link #MEDIA_INFO_VIDEO_TRACK_LAGGING}
         * 		<li>{@link #MEDIA_INFO_VIDEO_RENDERING_START}
         * 		<li>{@link #MEDIA_INFO_BUFFERING_START}
         * 		<li>{@link #MEDIA_INFO_BUFFERING_END}
         * 		<li><code>MEDIA_INFO_NETWORK_BANDWIDTH (703)</code> -
         * 		bandwidth information is available (as <code>extra</code> kbps)
         * 		<li>{@link #MEDIA_INFO_BAD_INTERLEAVING}
         * 		<li>{@link #MEDIA_INFO_NOT_SEEKABLE}
         * 		<li>{@link #MEDIA_INFO_METADATA_UPDATE}
         * 		<li>{@link #MEDIA_INFO_UNSUPPORTED_SUBTITLE}
         * 		<li>{@link #MEDIA_INFO_SUBTITLE_TIMED_OUT}
         * 		</ul>
         * @param extra
         * 		an extra code, specific to the info. Typically
         * 		implementation dependent.
         * @return True if the method handled the info, false if it didn't.
        Returning false, or not having an OnInfoListener at all, will
        cause the info to be discarded.
         */
        boolean onInfo(android.media.MediaPlayer mp, int what, int extra);
    }

    /**
     * Register a callback to be invoked when an info/warning is available.
     *
     * @param listener
     * 		the callback that will be run
     */
    public void setOnInfoListener(android.media.MediaPlayer.OnInfoListener listener) {
        mOnInfoListener = listener;
    }

    private android.media.MediaPlayer.OnInfoListener mOnInfoListener;

    /* Test whether a given video scaling mode is supported. */
    private boolean isVideoScalingModeSupported(int mode) {
        return (mode == android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT) || (mode == android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
    }

    /**
     *
     *
     * @unknown 
     */
    static class TimeProvider implements android.media.MediaPlayer.OnSeekCompleteListener , android.media.MediaTimeProvider {
        private static final java.lang.String TAG = "MTP";

        private static final long MAX_NS_WITHOUT_POSITION_CHECK = 5000000000L;

        private static final long MAX_EARLY_CALLBACK_US = 1000;

        private static final long TIME_ADJUSTMENT_RATE = 2;/* meaning 1/2 */


        private long mLastTimeUs = 0;

        private android.media.MediaPlayer mPlayer;

        private boolean mPaused = true;

        private boolean mStopped = true;

        private boolean mBuffering;

        private long mLastReportedTime;

        private long mTimeAdjustment;

        // since we are expecting only a handful listeners per stream, there is
        // no need for log(N) search performance
        private android.media.MediaTimeProvider.OnMediaTimeListener[] mListeners;

        private long[] mTimes;

        private long mLastNanoTime;

        private android.os.Handler mEventHandler;

        private boolean mRefresh = false;

        private boolean mPausing = false;

        private boolean mSeeking = false;

        private static final int NOTIFY = 1;

        private static final int NOTIFY_TIME = 0;

        private static final int REFRESH_AND_NOTIFY_TIME = 1;

        private static final int NOTIFY_STOP = 2;

        private static final int NOTIFY_SEEK = 3;

        private static final int NOTIFY_TRACK_DATA = 4;

        private android.os.HandlerThread mHandlerThread;

        /**
         *
         *
         * @unknown 
         */
        public boolean DEBUG = false;

        public TimeProvider(android.media.MediaPlayer mp) {
            mPlayer = mp;
            try {
                getCurrentTimeUs(true, false);
            } catch (java.lang.IllegalStateException e) {
                // we assume starting position
                mRefresh = true;
            }
            android.os.Looper looper;
            if (((looper = android.os.Looper.myLooper()) == null) && ((looper = android.os.Looper.getMainLooper()) == null)) {
                // Create our own looper here in case MP was created without one
                mHandlerThread = new android.os.HandlerThread("MediaPlayerMTPEventThread", android.os.Process.THREAD_PRIORITY_FOREGROUND);
                mHandlerThread.start();
                looper = mHandlerThread.getLooper();
            }
            mEventHandler = new android.media.MediaPlayer.TimeProvider.EventHandler(looper);
            mListeners = new android.media.MediaTimeProvider.OnMediaTimeListener[0];
            mTimes = new long[0];
            mLastTimeUs = 0;
            mTimeAdjustment = 0;
        }

        private void scheduleNotification(int type, long delayUs) {
            // ignore time notifications until seek is handled
            if (mSeeking && ((type == android.media.MediaPlayer.TimeProvider.NOTIFY_TIME) || (type == android.media.MediaPlayer.TimeProvider.REFRESH_AND_NOTIFY_TIME))) {
                return;
            }
            if (DEBUG)
                android.util.Log.v(android.media.MediaPlayer.TimeProvider.TAG, (("scheduleNotification " + type) + " in ") + delayUs);

            mEventHandler.removeMessages(android.media.MediaPlayer.TimeProvider.NOTIFY);
            android.os.Message msg = mEventHandler.obtainMessage(android.media.MediaPlayer.TimeProvider.NOTIFY, type, 0);
            mEventHandler.sendMessageDelayed(msg, ((int) (delayUs / 1000)));
        }

        /**
         *
         *
         * @unknown 
         */
        public void close() {
            mEventHandler.removeMessages(android.media.MediaPlayer.TimeProvider.NOTIFY);
            if (mHandlerThread != null) {
                mHandlerThread.quitSafely();
                mHandlerThread = null;
            }
        }

        /**
         *
         *
         * @unknown 
         */
        protected void finalize() {
            if (mHandlerThread != null) {
                mHandlerThread.quitSafely();
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public void onPaused(boolean paused) {
            synchronized(this) {
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "onPaused: " + paused);

                if (mStopped) {
                    // handle as seek if we were stopped
                    mStopped = false;
                    mSeeking = true;
                    /* delay */
                    scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_SEEK, 0);
                } else {
                    mPausing = paused;// special handling if player disappeared

                    mSeeking = false;
                    /* delay */
                    scheduleNotification(android.media.MediaPlayer.TimeProvider.REFRESH_AND_NOTIFY_TIME, 0);
                }
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public void onBuffering(boolean buffering) {
            synchronized(this) {
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "onBuffering: " + buffering);

                mBuffering = buffering;
                /* delay */
                scheduleNotification(android.media.MediaPlayer.TimeProvider.REFRESH_AND_NOTIFY_TIME, 0);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public void onStopped() {
            synchronized(this) {
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "onStopped");

                mPaused = true;
                mStopped = true;
                mSeeking = false;
                mBuffering = false;
                /* delay */
                scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_STOP, 0);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        @java.lang.Override
        public void onSeekComplete(android.media.MediaPlayer mp) {
            synchronized(this) {
                mStopped = false;
                mSeeking = true;
                /* delay */
                scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_SEEK, 0);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public void onNewPlayer() {
            if (mRefresh) {
                synchronized(this) {
                    mStopped = false;
                    mSeeking = true;
                    mBuffering = false;
                    /* delay */
                    scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_SEEK, 0);
                }
            }
        }

        private synchronized void notifySeek() {
            mSeeking = false;
            try {
                long timeUs = getCurrentTimeUs(true, false);
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "onSeekComplete at " + timeUs);

                for (android.media.MediaTimeProvider.OnMediaTimeListener listener : mListeners) {
                    if (listener == null) {
                        break;
                    }
                    listener.onSeek(timeUs);
                }
            } catch (java.lang.IllegalStateException e) {
                // we should not be there, but at least signal pause
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "onSeekComplete but no player");

                mPausing = true;// special handling if player disappeared

                /* refreshTime */
                notifyTimedEvent(false);
            }
        }

        private synchronized void notifyTrackData(android.util.Pair<android.media.SubtitleTrack, byte[]> trackData) {
            android.media.SubtitleTrack track = trackData.first;
            byte[] data = trackData.second;
            /* eos */
            /* runID: keep forever */
            track.onData(data, true, ~0);
        }

        private synchronized void notifyStop() {
            for (android.media.MediaTimeProvider.OnMediaTimeListener listener : mListeners) {
                if (listener == null) {
                    break;
                }
                listener.onStop();
            }
        }

        private int registerListener(android.media.MediaTimeProvider.OnMediaTimeListener listener) {
            int i = 0;
            for (; i < mListeners.length; i++) {
                if ((mListeners[i] == listener) || (mListeners[i] == null)) {
                    break;
                }
            }
            // new listener
            if (i >= mListeners.length) {
                android.media.MediaTimeProvider.OnMediaTimeListener[] newListeners = new android.media.MediaTimeProvider.OnMediaTimeListener[i + 1];
                long[] newTimes = new long[i + 1];
                java.lang.System.arraycopy(mListeners, 0, newListeners, 0, mListeners.length);
                java.lang.System.arraycopy(mTimes, 0, newTimes, 0, mTimes.length);
                mListeners = newListeners;
                mTimes = newTimes;
            }
            if (mListeners[i] == null) {
                mListeners[i] = listener;
                mTimes[i] = android.media.MediaTimeProvider.NO_TIME;
            }
            return i;
        }

        public void notifyAt(long timeUs, android.media.MediaTimeProvider.OnMediaTimeListener listener) {
            synchronized(this) {
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "notifyAt " + timeUs);

                mTimes[registerListener(listener)] = timeUs;
                /* delay */
                scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_TIME, 0);
            }
        }

        public void scheduleUpdate(android.media.MediaTimeProvider.OnMediaTimeListener listener) {
            synchronized(this) {
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "scheduleUpdate");

                int i = registerListener(listener);
                if (!mStopped) {
                    mTimes[i] = 0;
                    /* delay */
                    scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_TIME, 0);
                }
            }
        }

        public void cancelNotifications(android.media.MediaTimeProvider.OnMediaTimeListener listener) {
            synchronized(this) {
                int i = 0;
                for (; i < mListeners.length; i++) {
                    if (mListeners[i] == listener) {
                        java.lang.System.arraycopy(mListeners, i + 1, mListeners, i, (mListeners.length - i) - 1);
                        java.lang.System.arraycopy(mTimes, i + 1, mTimes, i, (mTimes.length - i) - 1);
                        mListeners[mListeners.length - 1] = null;
                        mTimes[mTimes.length - 1] = android.media.MediaTimeProvider.NO_TIME;
                        break;
                    } else
                        if (mListeners[i] == null) {
                            break;
                        }

                }
                /* delay */
                scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_TIME, 0);
            }
        }

        private synchronized void notifyTimedEvent(boolean refreshTime) {
            // figure out next callback
            long nowUs;
            try {
                nowUs = getCurrentTimeUs(refreshTime, true);
            } catch (java.lang.IllegalStateException e) {
                // assume we paused until new player arrives
                mRefresh = true;
                mPausing = true;// this ensures that call succeeds

                nowUs = getCurrentTimeUs(refreshTime, true);
            }
            long nextTimeUs = nowUs;
            if (mSeeking) {
                // skip timed-event notifications until seek is complete
                return;
            }
            if (DEBUG) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("notifyTimedEvent(").append(mLastTimeUs).append(" -> ").append(nowUs).append(") from {");
                boolean first = true;
                for (long time : mTimes) {
                    if (time == android.media.MediaTimeProvider.NO_TIME) {
                        continue;
                    }
                    if (!first)
                        sb.append(", ");

                    sb.append(time);
                    first = false;
                }
                sb.append("}");
                android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, sb.toString());
            }
            java.util.Vector<android.media.MediaTimeProvider.OnMediaTimeListener> activatedListeners = new java.util.Vector<android.media.MediaTimeProvider.OnMediaTimeListener>();
            for (int ix = 0; ix < mTimes.length; ix++) {
                if (mListeners[ix] == null) {
                    break;
                }
                if (mTimes[ix] <= android.media.MediaTimeProvider.NO_TIME) {
                    // ignore, unless we were stopped
                } else
                    if (mTimes[ix] <= (nowUs + android.media.MediaPlayer.TimeProvider.MAX_EARLY_CALLBACK_US)) {
                        activatedListeners.add(mListeners[ix]);
                        if (DEBUG)
                            android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "removed");

                        mTimes[ix] = android.media.MediaTimeProvider.NO_TIME;
                    } else
                        if ((nextTimeUs == nowUs) || (mTimes[ix] < nextTimeUs)) {
                            nextTimeUs = mTimes[ix];
                        }


            }
            if ((nextTimeUs > nowUs) && (!mPaused)) {
                // schedule callback at nextTimeUs
                if (DEBUG)
                    android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, (("scheduling for " + nextTimeUs) + " and ") + nowUs);

                scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_TIME, nextTimeUs - nowUs);
            } else {
                mEventHandler.removeMessages(android.media.MediaPlayer.TimeProvider.NOTIFY);
                // no more callbacks
            }
            for (android.media.MediaTimeProvider.OnMediaTimeListener listener : activatedListeners) {
                listener.onTimedEvent(nowUs);
            }
        }

        private long getEstimatedTime(long nanoTime, boolean monotonic) {
            if (mPaused) {
                mLastReportedTime = mLastTimeUs + mTimeAdjustment;
            } else {
                long timeSinceRead = (nanoTime - mLastNanoTime) / 1000;
                mLastReportedTime = mLastTimeUs + timeSinceRead;
                if (mTimeAdjustment > 0) {
                    long adjustment = mTimeAdjustment - (timeSinceRead / android.media.MediaPlayer.TimeProvider.TIME_ADJUSTMENT_RATE);
                    if (adjustment <= 0) {
                        mTimeAdjustment = 0;
                    } else {
                        mLastReportedTime += adjustment;
                    }
                }
            }
            return mLastReportedTime;
        }

        public long getCurrentTimeUs(boolean refreshTime, boolean monotonic) throws java.lang.IllegalStateException {
            synchronized(this) {
                // we always refresh the time when the paused-state changes, because
                // we expect to have received the pause-change event delayed.
                if (mPaused && (!refreshTime)) {
                    return mLastReportedTime;
                }
                long nanoTime = java.lang.System.nanoTime();
                if (refreshTime || (nanoTime >= (mLastNanoTime + android.media.MediaPlayer.TimeProvider.MAX_NS_WITHOUT_POSITION_CHECK))) {
                    try {
                        mLastTimeUs = mPlayer.getCurrentPosition() * 1000L;
                        mPaused = (!mPlayer.isPlaying()) || mBuffering;
                        if (DEBUG)
                            android.util.Log.v(android.media.MediaPlayer.TimeProvider.TAG, ((mPaused ? "paused" : "playing") + " at ") + mLastTimeUs);

                    } catch (java.lang.IllegalStateException e) {
                        if (mPausing) {
                            // if we were pausing, get last estimated timestamp
                            mPausing = false;
                            getEstimatedTime(nanoTime, monotonic);
                            mPaused = true;
                            if (DEBUG)
                                android.util.Log.d(android.media.MediaPlayer.TimeProvider.TAG, "illegal state, but pausing: estimating at " + mLastReportedTime);

                            return mLastReportedTime;
                        }
                        // TODO get time when prepared
                        throw e;
                    }
                    mLastNanoTime = nanoTime;
                    if (monotonic && (mLastTimeUs < mLastReportedTime)) {
                        /* have to adjust time */
                        mTimeAdjustment = mLastReportedTime - mLastTimeUs;
                        if (mTimeAdjustment > 1000000) {
                            // schedule seeked event if time jumped significantly
                            // TODO: do this properly by introducing an exception
                            mStopped = false;
                            mSeeking = true;
                            /* delay */
                            scheduleNotification(android.media.MediaPlayer.TimeProvider.NOTIFY_SEEK, 0);
                        }
                    } else {
                        mTimeAdjustment = 0;
                    }
                }
                return getEstimatedTime(nanoTime, monotonic);
            }
        }

        private class EventHandler extends android.os.Handler {
            public EventHandler(android.os.Looper looper) {
                super(looper);
            }

            @java.lang.Override
            public void handleMessage(android.os.Message msg) {
                if (msg.what == android.media.MediaPlayer.TimeProvider.NOTIFY) {
                    switch (msg.arg1) {
                        case android.media.MediaPlayer.TimeProvider.NOTIFY_TIME :
                            /* refreshTime */
                            notifyTimedEvent(false);
                            break;
                        case android.media.MediaPlayer.TimeProvider.REFRESH_AND_NOTIFY_TIME :
                            /* refreshTime */
                            notifyTimedEvent(true);
                            break;
                        case android.media.MediaPlayer.TimeProvider.NOTIFY_STOP :
                            notifyStop();
                            break;
                        case android.media.MediaPlayer.TimeProvider.NOTIFY_SEEK :
                            notifySeek();
                            break;
                        case android.media.MediaPlayer.TimeProvider.NOTIFY_TRACK_DATA :
                            notifyTrackData(((android.util.Pair<android.media.SubtitleTrack, byte[]>) (msg.obj)));
                            break;
                    }
                }
            }
        }
    }
}

