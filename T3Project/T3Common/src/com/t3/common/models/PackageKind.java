package com.t3.common.models;

/**
 * This class contains constant of Transfer object send/receive to server/client
 * @author Luan Vu
 */
public interface PackageKind {

	/** none */
	public static final int NONE 					= 0x00000000;
	
	/** Constant for request login */
	public static final int ACTION_LOGIN 			= 0x00000001;
	
	/** Constant for request logout */
	public static final int ACTION_LOGOUT			= 0x00000002;
	
	/** Constant for request get user list */
	public static final int ACTION_GET_USER_LIST 	= 0x00000003;
	
	/** Constant for request get board list */
	public static final int ACTION_GET_BOARD_LIST 	= 0x00000004;
	
	/** Constant for request create a board */
	public static final int ACTION_CREATE_BOARD		= 0x00000005;
	
	/** Constant for request join a board */
	public static final int ACTION_JOIN_BOARD		= 0x00000006;
	
	/** Constant for request send message in room */
	public static final int ACTION_SEND_ROOM_MESSAGE = 0x00000007;
	
	/** Constant for request send message in board */
	public static final int ACTION_SEND_BOARD_MESSAGE 	= 0x00000008;
	
	/** Constant for request create user */
	public static final int ACTION_CREATE_USER		 	= 0x00000009;
	
	/** Constant for request to client update user list */
	public static final int ACTION_ROOM_UPDATE_USER_LIST 	=   0x0000000A;
	
	/** Constant for request to client update board list */
	public static final int ACTION_ROOM_UPDATE_BOARD_LIST 	=  0x0000000B;
	
	/** Constant for request update data in board */
	public static final int ACTION_BOARD_UPDATE_DATA		=  0x0000000C;
	
	/** Constant for request leave board */
	public static final int ACTION_BOARD_LEAVE 				=  0x0000000D;
	
	/** Constant for request start board */
	public static final int ACTION_BOARD_START				=  0x0000000E;
	
	/** Constant for request ready in board */
	public static final int ACTION_BOARD_READY				=  0x0000000F;
	
	/** Constant for request not ready in board */
	public static final int ACTION_BOARD_UNREADY			=  0x00000016;
	
	/** Constant for request player tick */
	public static final int ACTION_BOARD_PLAYER_TICK		=  0x00000012;
	
	/** Constant for response to client win  */
	public static final int ACTION_BOARD_YOU_WON			=  0x00000013;
	
	/** Constant for response to client lose  */
	public static final int ACTION_BOARD_YOU_LOSE			=  0x00000014;
	
	/** Constant for response to client draw  */
	public static final int ACTION_BOARD_YOU_DRAW			=  0x00000015;
	
	/** Constant request client input board password */
	public static final int ACTION_INPUT_PASS_BOARD			=  0x00000017;
	
//	public static final int ACTION_
	
	/** Constant for return to client result login success */
	public static final int RESULT_LOGIN_SUCCESS 		= 0x000001F4;
	
	/** Constant for return to client result login userid in used */
	public static final int RESULT_LOGIN_USERID_USED 	= 0x000001F5;
	
	/** Constant for return to client result login userid fail */
	public static final int RESULT_LOGIN_USERID_FAIL	= 0x000001F6;
			
	/** Constant for return to client result a list user */
	public static final int RESULT_GET_USER_LIST		= 0x000001F7;
	
	/** Constant for return to client result a list board */
	public static final int RESULT_GET_BOARD_LIST		= 0x000001F8;
	
	/** Constant for return to client result create user success */
	public static final int RESULT_CREATE_USER_SUCCESS  = 0x000001F9;
	
	/** Constant for return to client result create user existed */
	public static final int RESULT_CREATE_USER_EXISTED  = 0x000001FA;
	
	/** Constant for return to client result create user fail */
	public static final int RESULT_CREATE_USER_FAIL		= 0x000001FB;
	
	/** Constant for return to client result create board success */
	public static final int RESULT_CREATE_BOARD_SUCCESS = 0x000001FC;
	
	/** Constant for return to client result create board fail */
	public static final int RESULT_CREATE_BOARD_FAIL	= 0x000001FD;
	
	
	/* Nam 2013 - 11 - 8 */
	public static final int RESULT_MESSAGE_ROOM			= 0x000001FE;
	public static final int RESULT_MESSAGE_BOARD		= 0x000001FF;
	
	/* Nam 2013 - 11 - 9 */
	public static final int RESULT_JOIN_BOARD_SUCCESS 	= 0x00000200;
	
	public static final int RESULT_JOIN_BOARD_FULL		= 0x00000201;
	
	public static final int RESULT_JOIN_BOARD_FAIL		= 0x00000202;
	
	public static final int RESULT_JOIN_BOARD_PASSW_WRONG		= 0x00000203;
	
	/* Vu 2013 - 11 - 9 */
	public static final int RESULT_JOIN_BOARD_NOT_EXIST					= 0x00000204;
	
	public static final int RESULT_BOARD_LEAVE_OK						= 0x00000205;
	
	public static final int RESULT_BOARD_START_OK						= 0x00000206;
	
	public static final int RESULT_BOARD_START_FAIL						= 0x00000207;
	
	public static final int RESULT_BOARD_START_PLAYER_NOT_READY			= 0x00000208;
	
	public static final int RESULT_BOARD_START_NOT_ENOUGH_PLAYER		= 0x00000209;
	
	public static final int RESULT_BOARD_READY_OK						= 0x0000020A;
	
	public static final int RESULT_BOARD_UNREADY_OK						= 0x0000020B;
	
	//// Nam 2013 - 11 - 09- (10:13 PM)
	public static final int RESULT_PASSWORD_BOARD			= 0x0000020C;
	
	/** Constant for return to client result logout success */
	public static final int RESULT_LOGOUT_OK 			= 0x0000020D;
	
	
	
//	public static final int RESULT_
	
	

}
