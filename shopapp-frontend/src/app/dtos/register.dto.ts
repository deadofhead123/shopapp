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

    facebook_account_id: number = 0;
    google_account_id: number = 0;
    role_id: number = 2;

    constructor() {
        this.phone_number = '';
        this.password = '';
        this.retypePassword = '';
        this.fullname = '';
        this.address = '';
        this.date_of_birth = new Date();
    }
}