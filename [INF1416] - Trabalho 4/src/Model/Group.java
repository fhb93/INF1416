package Model;

//Classe do grupo do Usuário - administrador ou usuario
public enum Group {
    ADMIN(0),
    USER(1);

    Group(int index) {}

    public static Group getGroup(int index) {
        switch(index) {
            case 0:
                return ADMIN;
            case 1:
                return USER;
        }
        return null;
    }
}
