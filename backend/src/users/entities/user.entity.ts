import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  gender: string;

  @Column()
  firstName: string;

  @Column()
  lastName: string;

  @Column()
  email: string;

  @Column()
  password: string;

  @Column()
  phoneNumber: string;

  @Column()
  dateOfBirth: string;

  @Column({ default: false })
  isPremium: boolean;

  @Column({ default: false })
  isCustomer: boolean;

  @Column({ default: false })
  isProvider: boolean;
}
