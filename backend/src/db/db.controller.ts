import { Controller, Get, Header, Res } from '@nestjs/common';
import { Response } from 'express';
import { DbService } from './db.service';

@Controller('db')
export class DbController {
  constructor(private dbService: DbService) {}

  @Get()
  @Header('Content-Type', 'application/json')
  getDbConnection(@Res() res: Response) {
    return this.dbService.canConnect(res);
  }
}
