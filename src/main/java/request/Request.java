package request;

public class Request {
    public static final int LOGIN             = 1;  //fazer login
    public static final int LOGOUT            = 2; //Logout
    public static final int REGISTER          = 3; //fazer registro
    public static final int MANAGE            = 4;  //gerir artistas, musicas e albuns
    public static final int SEARCH            = 5;  //procurar musicas por album ou artista
    public static final int CRITIQUE          = 6;  //escrever critica a album
    public static final int MAKE_EDITOR       = 7;  //dar privilegios de editor a user
    public static final int UPLOAD            = 8; //fazer upload de uma musica para o server
    public static final int SHARE             = 9; //Share de uma musica com users
    public static final int DOWNLOAD          = 10; //Download de musicas do servidor
    public static final int ADD_PLAYLIST      = 13; //Criar nova playlist
    public static final int REMOVE_PLAYLIST   = 14; //Remover uma playlist
    public static final int SHARE_PLAYLIST    = 15; //Tornar playlist publica/privada
    public static final int ADD_MUSIC_PLAYLIST= 16; //Adicionar musicas a uma playlist
    public static final int DEL_MUSIC_PLAYLIST= 17; //Remover uma musica de uma playlist
    public static final int ADD_MUSIC_ALBUM   = 18; //Adicionar musicas a um album
    public static final int DEL_MUSIC_ALBUM   = 19; //Remover de um album
    public static final int REMOVE_ITEM       = 20; //Remover um item

    //Debug mode
    public static final boolean DEV_MODE = true;
}
