
# CONVECTIONS :

ViewType Class Variable's :
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"mTypeName"** --------------------------- e.g. mTextViewDisplayname

Classnames :
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"TypeName"** ------------------------------ e.g. ActivityMain, FragmentPostMedia, etc

Event Listener Names :
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"OnTypeListener"** ----------------------- e.g. OnFeedCompleteListener

XML layout's :
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;layouts/activities/fragments
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"type_name"** -------------------------- e.g. activity_main.xml, fragment_posting_media, etc
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;drawables
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"category_name"** -------------------- e.g. ic_heart.xml
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"category_category_name"** ------ e.g. background_round_corners.xml

@+id names :
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**"@+id/type_name"** --------------------- e.g. "@+id/button_logout", "@+id/textview_displayname", etc
	
# FIRESTORE STRUCTURE :

    CHATS
    ├── <id1>
    │   ├── name "name"
    │   ├── type "personal/group"
	│   ├── participants
	│	│	├── 0 <uid>
	│	│	└── 1 <uid>
	│	└── messages
	│	    ├──	<id1>
	│		│   └── ...
	│		└── <id2>
	│		    ├──	timestamp 23432423
	│		    ├──	author <uid>
	│		    ├──	author_displayname "display name"
	│		    └──	message "message"
    ├── <id2>
    │   └── ...
    └── <id2>
        └── ...
    POSTS
	├── <id2>
	│   └──	...
    ├──	<id3>
	│   ├── type "STATUS" //MEDIA,STATUS,THREAD;
	│   ├──	author <uid>
	│   ├──	author_displayname
	│   ├──	author_username
	│   ├──	comments ##
	│   ├──	likes ##
	│   ├──	reposts ##
	│   ├──	hashtags
	│   │	└──	0 "#example"
	│   └── media
	│    	└──	0 "something.com/src"
	└── <id4>
	    └── ...
    ACCOUNTS
	├── <uid1>
	│   └──	...
    ├──	<uid2>
	│   ├── displayname "fdsn amefdsf"
	│   ├── username "loginname"
	│   ├── email "email@com.com"
	│   ├── phone "43354435"
	│   ├── type "art"
	│   ├── website "www.exmaple.com"
	│   ├── summary "account summary / description" 
	│   ├── img_src_profile "fkjsdl.www/fgsd"
	│   ├── img_src_banner "fkjsdl.www/fgsdfd"
	│	├── following 
	│	│   ├── 0 <uid1>
	│	│   └── 1 <uid2>
	│   └── followers
	│	    ├── 0 <uid1>
	│	    ├── 1 <uid2>
	│    	└── 2 <uid3>
	└── <uid1>
	    └──	...
	    <id4>
	    	...
