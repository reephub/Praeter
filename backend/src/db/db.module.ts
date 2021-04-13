import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { User } from 'src/users/entities/user.entity';
import { UsersModule } from './../users/users.module';
import { DbController } from './db.controller';
import { DbService } from './db.service';

@Module({
  imports: [TypeOrmModule.forFeature([User]), UsersModule],
  exports: [TypeOrmModule],
  controllers: [DbController],
  providers: [DbService],
})
export class DbModule {}
