/**
 * Package Description : Server.
 *
 * (WIP) This package described all the classes enabling a server-clients relations.
 * The connection currently work very well for simple network (192.168.x.x), but wasn't tested for other types of networks.
 * The client will establish connection be trying to reach an open port on IPs of the same network.
 * This is done in 2 steps :
 * <ol>
 *     <li>Scanning for similar IPs on standard port (If IP is X.X.1.1, search for IPs from X.X.1.0 to X.X.1.255 on standard port)</li>
 *     <li>Scanning the whole network on the whole port range if no result from previous try</li>
 * </ol>
 * The whole search is done in parallel and can fail if the time to reach the server is too long because of the set timeout (a longer timeout make it less likely to fail to reach a server, but make the whole process longer).
 */
package com.view.server;