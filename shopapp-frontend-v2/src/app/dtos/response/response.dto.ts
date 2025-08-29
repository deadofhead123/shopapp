export type ResponseDTO<D> = {
    data: D[] | D,
    message: string,
    status: number
}