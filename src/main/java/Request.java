public class Request {
    protected static final int LOGIN             = 1;  //fazer login
    protected static final int LOGOUT            = 2; //Logout
    protected static final int REGISTER          = 3; //fazer registro
    protected static final int MANAGE            = 4;  //gerir artistas, musicas e albuns
    protected static final int SEARCH            = 5;  //procurar musicas por album ou artista
    protected static final int CRITIQUE          = 6;  //escrever critica a album
    protected static final int MAKE_EDITOR       = 7;  //dar privilegios de editor a user
    protected static final int NOTE_EDITOR       = 8;  //notificação de novo editor
    protected static final int NOTE_NEW_EDIT     = 9;  //notificaçao de novo edit
    protected static final int NOTE_DELIVER      = 10;  //entregar notificação a user previ. off
    protected static final int UPLOAD            = 11; //fazer upload de uma musica para o server
    protected static final int SHARE             = 12; //Share de uma musica com users
    protected static final int DOWNLOAD          = 13; //Download de musicas do servidor
    protected static final int CALLBACK          = 14; //Packet returned after processing in server
    protected static final int UP_TCP            = 15; //Devolve o endereço do servidor para dar upload
    protected static final int DOWN_TCP          = 16; //Devolve o endereço do servidor para fazer download
    protected static final int NEW_SERVER        = 17; //Novo servidor criado, novos pacotes podem escolhe-lo
    protected static final int SERVER_DOWN       = 18; //Servidor foi abaixo, novos pacotes não o escolhem
    protected static final int UPLOAD_MUSIC      = 19; //Upload de musicas para o servidor
    protected static final int ADD_PLAYLIST      = 20; //Criar nova playlist
    protected static final int REMOVE_PLAYLIST   = 21; //Remover uma playlist
    protected static final int SHARE_PLAYLIST    = 22; //Tornar playlist publica/privada
    protected static final int ADD_MUSIC_PLAYLIST= 23; //Adicionar musicas a uma playlist

    //Debug mode
    public static final boolean DEV_MODE = true;
}
