class CommonUserViewModel {
    constructor(id, name, description, email) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.email = email;
    }

    toPlainObject() {
        return {
            id: this.id,
            name: this.name,
            description: this.description,
            email: this.email
        };
    }
}

export default CommonUserViewModel;