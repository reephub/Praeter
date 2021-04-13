import { HttpStatus, Injectable, Res } from '@nestjs/common';
import { Response } from 'express';
import { Connection } from 'typeorm';

@Injectable()
export class DbService {
  constructor(private connection: Connection) {}

  canConnect(@Res() res: Response) {
    if (!this.connection.connect) {
      console.log('Connection to the database impossible');

      return res.status(HttpStatus.INTERNAL_SERVER_ERROR).json({message : 'databse connection failed'});
      //return new Response('{"message" : "databse connection failed"}');
      // return {
      //   code: res.status(HttpStatus.INTERNAL_SERVER_ERROR),
      //   message: 'databse connection failed',
      // };
    } else {
      console.log('Connection succesfully done');
      return res.status(HttpStatus.OK).json({message :'databse connection successful'});
      //return '{"message" : "database connection successful}"';
      // return {
      //   code: res.status(HttpStatus.OK),
      //   message: 'databse connection successful',
      // };
    }
  }
}
