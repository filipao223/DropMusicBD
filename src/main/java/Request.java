public class Request {
    protected static final int LOGIN             = 1;  //fazer login
    protected static final int LOGOUT            = 2; //Logout
    protected static final int REGISTER          = 3; //fazer registro
    protected static final int MANAGE            = 4;  //gerir artistas, musicas e albuns
    protected static final int SEARCH            = 5;  //procurar musicas por album ou artista
    protected static final int DETAILS_ALBUM     = 6; //Consultar detalhes de album
    protected static final int DETAILS_ARTIST    = 7; //Consultar detalhes de artista
    protected static final int CRITIQUE          = 8;  //escrever critica a album
    protected static final int MAKE_EDITOR       = 9;  //dar privilegios de editor a user
    protected static final int NOTE_EDITOR       = 10;  //notificação de novo editor
    protected static final int NOTE_NEW_EDIT     = 11;  //notificaçao de novo edit
    protected static final int NOTE_DELIVER      = 12;  //entregar notificação a user previ. off
    protected static final int UPLOAD            = 13; //fazer upload de uma musica para o server
    protected static final int SHARE             = 14; //Share de uma musica com users
    protected static final int DOWNLOAD          = 15; //Download de musicas do servidor
    protected static final int CALLBACK          = 16; //Packet returned after processing in server
    protected static final int UP_TCP            = 17; //Devolve o endereço do servidor para dar upload
    protected static final int DOWN_TCP          = 18; //Devolve o endereço do servidor para fazer download
    protected static final int NEW_SERVER        = 19; //Novo servidor criado, novos pacotes podem escolhe-lo
    protected static final int SERVER_DOWN       = 20; //Servidor foi abaixo, novos pacotes não o escolhem
    protected static final int UPLOAD_MUSIC      = 21; //Upload de musicas para o servidor

    //Debug mode
    public static final boolean DEV_MODE = true;
}
