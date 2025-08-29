import { IsDate, IsNotEmpty, IsNumber, IsString } from "class-validator";

export class RegisterDTO {
    @IsString()
    fullname: string;

    @IsString()
    @IsNotEmpty()
    phone_number: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    @IsString()
    @IsNotEmpty()
    retypePassword: string;

    @IsString()
    address: string;

    @IsDate()
    date_of_birth: Date;

    facebook_account_id: number;
    google_account_id: number;
    role_id: number;

    constructor(data: any) { // Thay vì truyền 1 đống thuộc tính vào trong danh sách tham số, ta truyền 1 object vào thì nó nhanh hơn. 
        this.phone_number = data.phone_number;
        this.password = data.password;
        this.retypePassword = data.retypePassword;
        this.fullname = data.fullName;
        this.address = data.address;
        this.date_of_birth = data.date_of_birth;
        this.facebook_account_id = data.facebook_account_id | 0;
        this.google_account_id = data.google_account_id | 0;
        this.role_id = data.role_id | 2;
    }
}