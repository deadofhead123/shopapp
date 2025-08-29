import { IsDate, IsNotEmpty, IsNumber, IsString } from "class-validator";

export class LoginDTO {
    @IsString()
    @IsNotEmpty()
    phone_number: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    role_id: number;

    constructor(data: any) { // Thay vì truyền 1 đống thuộc tính vào trong danh sách tham số, ta truyền 1 object vào thì nó nhanh hơn. 
        this.phone_number = data.phone_number;
        this.password = data.password;
        this.role_id = data.role_id;
    }
}