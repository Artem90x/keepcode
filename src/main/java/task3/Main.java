package task3;

public class Main {

    void processTask(ChannelHandlerContext ctx) {

        InetSocketAddress lineAddress = new InetSocketAddress(getIpAddress(), getUdpPort());

        CommandType commandType;
        DblIncomeUssdMessage dblIncomeUssdMessage;

        String commandText;
        String hostString = lineAddress.getHostString();
        String hostName = lineAddress.getHostName();
        int port = lineAddress.getPort();

        for (Command currentCommand : getAllCommands()) {
            commandType = currentCommand.getCommandType();
            commandText = currentCommand.getCommandText();
            dblIncomeUssdMessage = new DblIncomeUssdMessage(
                    hostName,
                    port,
                    0,
                    EnumGoip.getByModel(getGoipModel()),
                    commandText);

            if (currentCommand.isAttemptsNumberExhausted()) {
                deleteCommand(commandType);

            } else if ((commandType.equals(CommandType.REBOOT_CHANNEL)
                    && currentCommand.isTimeToSend())
                    || !commandType.equals(CommandType.REBOOT_CHANNEL)) {
                sendCommandToContext(ctx, lineAddress, commandText);

                try {
                    AdminController.getInstance().processUssdMessage(dblIncomeUssdMessage, false);
                } catch (Exception ignored) {
                }
                currentCommand.setSendDate(new Date());
                Log.ussd.write(String.format(
                        "sent: ip: %s; порт: %d; %s",
                        hostString,
                        port,
                        commandText));
                currentCommand.incSendCounter();
            }
            sendKeepAliveOkAndFlush(ctx);
        }
    }
}